package burp;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;                                                                                                  
import java.io.OutputStream;

import com.veggiespam.imagelocationscanner.ILS;




/**
 * The Burp Pro plug-in wrapper for Veggiespam's Image
 * Location Scanner class. Passively scans a data stream containing 
 * a jpeg and reports if the data contains embedded Exif GPS location. 
 * 
 * @author  Jay Ball / github: veggiespam / twitter: @veggiespam / www.veggiespam.com
 * @license Apache License 2.0
 * @version 0.1
 * @see http://www.veggiespam.com/ils/
 */
public class BurpExtender implements IBurpExtender, IScannerCheck
{
	private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;                                                                                
    private OutputStream stdout;

	/** A bunch of static strings that are used by both ZAP and Burp plug-ins. 
	 * Burp requires modName be set that way. */
    private static final String modName = ILS.pluginName;
    private static final String alertTitle = ILS.alertTitle;
    private static final String issueDetailPrefix = ILS.alertDetailPrefix;
    private static final String issueBackground  = ILS.alertBackground;
    private static final String remediationBackground = ILS.remediationBackground;
    private static final String remediationDetail = ILS.remediationDetail;
    
    /** Used in some debug statements. */
    private static final String SEP = " | ";


    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
    	this.callbacks = callbacks;
        helpers = callbacks.getHelpers();
    	
    	callbacks.setExtensionName(modName);
    	callbacks.registerScannerCheck(this);
    	
        stdout = callbacks.getStdout();

    	db("plugin version " + ILS.pluginVersion);
        db("registerExtenderCallbacks() is complete");
    }
    
    /** Passive only, so this is a blank implementation. */
    @Override
    public List<IScanIssue> doActiveScan(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
    	return null;
    }
    
    @Override
    public List<IScanIssue> doPassiveScan(IHttpRequestResponse baseRequestResponse) {
    	//db("doPassiveScan()");
    	
        URL url = helpers.analyzeRequest(baseRequestResponse).getUrl();
        String mimeInferred = helpers.analyzeResponse(baseRequestResponse.getResponse()).getInferredMimeType();
        
        // inferred seems to work, no need for additional checking on stated types
        // String mimeStated = helpers.analyzeResponse(baseRequestResponse.getResponse()).getStatedMimeType();
        
        String fileName = url.getFile();
        
        /* The extension was online needed for stated processing, but we don't
         * need to do that.  So, ignore unless there is a need for it later if
         * a new condition is discovered
         */
        /* String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        //db(mimeStated + SEP + mimeInferred + SEP + extension);
        */
        
        
        // If body type is jpg, then we call the scanner on the response body
        if (mimeInferred.equalsIgnoreCase("JPEG")) {
            byte[] resp = baseRequestResponse.getResponse();
            int responseOffset = helpers.analyzeResponse(resp).getBodyOffset();
            //String responseBody = new String(baseRequestResponse.getResponse()).substring(responseOffset);
            byte[] body = Arrays.copyOfRange(resp,responseOffset, resp.length);
          
            //db("Parsing JPEG " + fileName);
            String hasGPS = ILS.scanForLocationInImage(body);
            if (! hasGPS.isEmpty()) {
            	db(fileName + ": found GPS: " + hasGPS);
                List<IScanIssue> alert = new ArrayList<IScanIssue>();
                IHttpRequestResponse[] x = new IHttpRequestResponse[1];
                x[0] = baseRequestResponse;
                
                alert.add(new CustomScanIssue(
                        baseRequestResponse.getHttpService(),
                        url,
                        x,
                		alertTitle,
                		issueDetailPrefix + hasGPS,
                        "Information",
                        "Certain",
                        issueBackground, remediationBackground, remediationDetail  // three static strings
                	)
                );
            	return alert; 
 
            } else {
            	; // no-op.  ignore it (or log it for debugging)
            	//db(fileName + ": No GPS");
            }
        }
        
        return null;
    }
    

    /** If the URL and Details match, then it is the same finding. */
    @Override
    public int consolidateDuplicateIssues(IScanIssue existingIssue, IScanIssue newIssue) {
        if (existingIssue.getIssueDetail().equals(newIssue.getIssueDetail()) 
        		&& existingIssue.getUrl().equals(newIssue.getUrl()) ) {
        	// Duplicate
            return -1;
        } else {
            return 0;
        }
    }
    
    /** The alert structure for bugs found.  This subclass is almost a cut-n-paste by August Detlefsen from 
     * https://code.google.com/p/burp-suite-software-version-checks/source/browse/trunk/src/burp/BurpExtender.java
     * (or his many other plugins, check them out).  His plugins are licensed with the Apache License 2.0.
     * We can use the same name for issue class since the class isn't exposed. */
    class CustomScanIssue implements IScanIssue {

        private IHttpService httpService;
        private URL url;
        private IHttpRequestResponse[] httpMessages;
        private String name;
        private String detail;
        private String severity;
        private String confidence;
        private String issueBackground;
        private String remediationBackground;
        private String remediationDetail;

        public CustomScanIssue(
                IHttpService httpService,
                URL url,
                IHttpRequestResponse[] httpMessages,
                String name,
                String detail,
                String severity,
                String confidence,
                String issueBackground,
                String remediationBackground,
                String remediationDetail    ) {
            this.httpService = httpService;
            this.url = url;
            this.httpMessages = httpMessages;
            this.name = name;
            this.detail = detail;
            this.severity = severity;
            this.confidence = confidence;
            this.issueBackground = issueBackground;
            this.remediationBackground = remediationBackground;
            this.remediationDetail = remediationDetail;
        }

        @Override
        public URL getUrl() {
            return url;
        }

        @Override
        public String getIssueName() {
            return name;
        }

        @Override
        public int getIssueType() {
            return 0;
        }

        @Override
        public String getSeverity() {
            return severity;
        }

        @Override
        public String getConfidence() {
            return confidence;
        }

        @Override
        public String getIssueBackground() {
            return issueBackground;
        }

        @Override
        public String getRemediationBackground() {
            return remediationBackground;
        }

        @Override
        public String getIssueDetail() {
            return detail;
        }

        @Override
        public String getRemediationDetail() {
            return remediationDetail;
        }

        @Override
        public IHttpRequestResponse[] getHttpMessages() {
            return httpMessages;
        }

        @Override
        public IHttpService getHttpService() {
            return httpService;
        }
    }

    
    /** Poor man's println to Burp's stdout, db means debug. */
    private void db(String d) {
    	d = modName.concat(": ").concat(d).concat("\n");
    	try { 
    		stdout.write(d.getBytes());
    	} catch(Exception e) {
    		// no op.
    	}
    }
}