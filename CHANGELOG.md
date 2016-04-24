Change Log
==========

Version 1.0.1 *(24-04-2016)*
------------------
  * Fetched Data: 
    * Phone NormalizedNumber, Type and Label are now distinct fields. (Were aggregated in previous version)
    * Email Address, Type and Label are now distinct fields (Were aggregated in previous version)
  * Implemented Query Constraints:
    * startsWith
    * equalTo
  * Added findFirst query function.
  * Implemented Comparator functionality. Allows you to provide a comparator to choose between values of same type in same contact.

Version 1.0.0 *(21-04-2016)*
------------------
 * Fetched Data: 
  * DisplayName
  * Phone NormalizedNumber and Type (and Label)
  * Email Address and Type (and Label)
  * PhotoURI
 * Implemented Query Constraints:
  * whereContains
  * hasPhoneNumber
  * include
