# Version History for Image Location and Privacy Scanner

Notable changes for humans to read.  The format is semi-based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).  See also [Contributors](CONTRIBUTORS.md) for people to thank and [TODO.md](TODO.md) for future ideas to implement. 

## 1.2 - 2025-06-01
- Updated build to Metadata Extractor 2.19.0 & Burp Extender API v2.3
    + Burp Extender is legacy API, need to migrate to Burp's Montoya API in future
- Added support for scanning HEIF image format used by modern iPhone images
- Minor safety update to fix theoretical missed file close by [@kingthorin](https://github.com/kingthorin) in [#22](https://github.com/veggiespam/ImageLocationScanner/issues/22) 
- Modernized Gradle build config to something supported in last decade
- Added support for Samsung, more Reconyxs, & Sony camera proprietary privacy leakage 
- Added detection for a few new information leakage tags in currently supported cameras.
- Added GPS elevation detection
- Added Markdown output support for [#24](https://github.com/veggiespam/ImageLocationScanner/issues/24) (*\*sponsored feature\**)
- Moved changelog to KeepAChangelog pretty format
- Tons of typo updates and better English
- Fixed issue [#26](https://github.com/veggiespam/ImageLocationScanner/issues/26) where newer versions Burp are not setting mimeInferred in many instances, so ILS never gets called to scan valid jpeg images.
- Split TODO into [TODO.md](TODO.md) since [README.md](README.md) was getting long
- Full removed XMP support as it was unreliable (was partially removed before)

## 1.1 - 2019-02-21
- Your basic corrections of spelling errors in comments when you finally use something besides vim for writing code
- Break out README.md into CONTRIBUTORS.md and CHANGELOG.md - readme was too long
- New IPTC privacy tags leaks detected
- Updated dependencies to Metadata Extractor 2.13.0 & XMP Core 6.0.6 & Burp Extender API v2.1
- Minor speedup in critical path
- Gradle build now targets Java 9 API

## 1.0 - 2018-01-22
- Gradle build automatically downloads the Burp API jar, so no need to include code in Git repo any longer
- Fixed mixed spaces-and-tabs, thanks to ZAP's [@kingthorin](https://github.com/kingthorin)
- Fixed a chance of an image causing HTML-injection inside of Burp; I theorized it existed (maybe a non-Burp app calling ILS would result in full-blown XSS against the infosec tester), but [@pajswigger](https://github.com/pajswigger) from Burp/Portswigger actually exploited this type of injection in the form of `<i>` tags, since Burp rejects `<script>` tags
- Nicer Makefile (sigh, yes, I still use make)
- Enhanced READMEs, FAQs, screenshots, etc
- ZAP now auto-scans images without the need to "un-hide" images
- Lots of unit tests via junit inside of ZAP, [@kingthorin](https://github.com/kingthorin) helped a bit

## 0.4 - 2017-08-22
- New official name: ***Image Location and Privacy Scanner***
- Updated to MetaData Extractor 2.10.1 & XMP Core 6.1.10
- Some XMP support removed via MDE; XMP tags weren't correct in some cases.  Those tags will be introduced again in a future MDE
- Removed legacy jar dependencies
- Build process is now Gradle only, Makefile is dead
- Added display of camera serial numbers for Leica, Reconyx Hyper Fire, Reconyx Ultra Fire
- Now shows name and age of facial recognition in Panasonic cameras

## 0.3 - 2016-11-03
- Fixed bugs where some codes Proprietary Camera codes were displayed as ID numbers instead of text
- Strip out tag that are \\0 null values or array of nulls
- More testing of IPTC with good results
- Updated to MetaData Extractor 2.9.1 for new XMP embedded in Exif tag support and other bug fixes
- Detect multiple instances of categories, for example, if there are many sets of Exif GPS records, all are displayed
- Added display of camera serial numbers FujiFilm, Nikon, Olympus, Canon, Sigma
- Added display of camera owner name for Canon
- Added support for HTML formatting in the Burp output
- Command line version output in text or HTML formats

## 0.2 - 2016-06-02
- Added location scanning inside IPTC tags and proprietary Panasonic codes
- Added scanning of png and tiff files
- Replaced Apache Sanselan with MetaData Extractor and Adobe XMP libraries

## 0.1 - 2016-01-26
- Initial release
- It works

<!--
vim: autoindent noexpandtab tabstop=4 shiftwidth=4
-->
