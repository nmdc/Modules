<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================== -->
<!-- A translator for DIF (GCMD) to ISO 19115 -->
<!-- Written by Dave Connell (Australian Antarctic Data Centre) and Andy Townsend (Australian Antarctic Data Centre) -->
<!-- Released on the 5th of June, 2008.  Last updated on the 6th of March, 2009 -->
<!-- Version 2.1 -->
<!-- ====================================================== -->

<!-- Trap for young players - name space definitions must match those served out of geoserver -->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:nmdc="http://www.nmdc.org/2015/01/Metadata"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gmx="http://www.isotc211.org/2005/gmx"
                xmlns:srv="http://www.isotc211.org/2005/srv"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:dif="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:gml="http://www.opengis.net/gml">
    <xsl:strip-space elements="*"/>
    <xsl:param name="identifer"/>    
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" media-type="text/xml"/>

    <!-- MATCH ROOT DIF -->
    <xsl:template match="gmd:MD_Metadata">
        <nmdc:meta>
            <nmdc:nmdc-metadata>
                <dif:DIF>
                    <dif:Entry_ID>
                        <xsl:value-of select="gmd:fileIdentifier/gco:CharacterString" />
                    </dif:Entry_ID>
                    <dif:Entry_Title>
                        <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString" />
                    </dif:Entry_Title>

                    <xsl:for-each select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:descriptiveKeywords">
                        <xsl:for-each select="./gmd:MD_Keywords">
                            <xsl:if test="not(//gmd:thesaurusName) or ./gmd:thesaurusName/gmd:CI_Citation/gmd:title/gco:CharacterString = 'NASA/GCMD Science Keywords'">
                                <xsl:for-each select="./gmd:keyword">                                                                
                                    <dif:Parameters>
                                        <dif:Category>EARTH SCIENCE</dif:Category>
                                        <xsl:variable name="str" as="xs:string" select="./gco:CharacterString" />
                                        <xsl:variable name="delim" as="xs:string" select="'&gt;'" />
                                        <dif:Topic>
                                            <xsl:value-of select="normalize-space(replace(upper-case(normalize-space(tokenize($str,$delim)[1])), $delim, ''))" />
                                        </dif:Topic>
                                        <dif:Term>
                                            <xsl:value-of select="normalize-space(replace(upper-case(normalize-space(tokenize($str,$delim)[2])), $delim, ''))" />
                                        </dif:Term>
                                        <dif:Variable_Level_1>
                                            <xsl:value-of select="normalize-space(replace(upper-case(normalize-space(tokenize($str,$delim)[3])), $delim, ''))" />
                                        </dif:Variable_Level_1>
                                    </dif:Parameters>                                

                                </xsl:for-each>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:for-each>
                    <dif:Temporal_Coverage>
                        <dif:Start_Date>
                            <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:temporalElement/gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod/gml:beginPosition" />
                        </dif:Start_Date>
                        <dif:Stop_Date>
                            <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:temporalElement/gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod/gml:endPosition" />
                        </dif:Stop_Date>
                    </dif:Temporal_Coverage>
                    <dif:Data_Set_Progress>Finished</dif:Data_Set_Progress>
                    <dif:Spatial_Coverage>
                        <dif:Southernmost_Latitude>
                            <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:southBoundLatitude/gco:Decimal" />
                        </dif:Southernmost_Latitude>
                        <dif:Northernmost_Latitude>
                            <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:northBoundLatitude/gco:Decimal" />
                        </dif:Northernmost_Latitude>
                        <dif:Westernmost_Longitude>
                            <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:westBoundLongitude/gco:Decimal" />
                        </dif:Westernmost_Longitude>
                        <dif:Easternmost_Longitude>
                            <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:eastBoundLongitude/gco:Decimal" />
                        </dif:Easternmost_Longitude>
                        <dif:Minimum_Altitude />
                        <dif:Maximum_Altitude />
                        <dif:Minimum_Depth />
                        <dif:Maximum_Depth />
                    </dif:Spatial_Coverage>
                    <dif:Data_Set_Language>
                        <xsl:if test="gmd:language/gmd:LanguageCode/@codeListValue = 'eng'">en</xsl:if>
                        <xsl:if test="gmd:language/gmd:LanguageCode/@codeListValue = 'nor'">no</xsl:if>
                    </dif:Data_Set_Language>
                    <dif:Originating_Center>
                        <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString" />
                    </dif:Originating_Center>
                    <dif:Data_Center>
                        <dif:Data_Center_Name>
                            <dif:Short_Name>
                                <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString" />
                            </dif:Short_Name>
                            <dif:Long_Name>
                                <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString" />
                            </dif:Long_Name>
                        </dif:Data_Center_Name>
                        <dif:Data_Set_ID>
                            <xsl:value-of select="gmd:fileIdentifier/gco:CharacterString" />
                        </dif:Data_Set_ID>
                        <dif:Personel>
                            <dif:Role></dif:Role>
                            <dif:First_Name></dif:First_Name>
                            <dif:Middle_Name></dif:Middle_Name>
                            <dif:Last_Name>
                                <xsl:value-of select="gmd:identificationInfo/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:individualName/gco:CharacterString" />
                            </dif:Last_Name>
                            <dif:Email>
                                <xsl:value-of select="gmd:identificationInfo/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:electronicMailAddress/gco:CharacterString" />
                            </dif:Email>
                            <dif:Phone>
                                <xsl:value-of select="gmd:identificationInfo/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:phone/gmd:CI_Telephone/gmd:voice/gco:CharacterString" />
                            </dif:Phone>
                            <dif:Fax>
                                <xsl:value-of select="gmd:identificationInfo/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:phone/gmd:CI_Telephone/gmd:facsimile/gco:CharacterString" />
                            </dif:Fax>
                            <dif:Contact_Address>
                                <dif:Address>
                                    <xsl:value-of select="gmd:identificationInfo/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:deliveryPoint/gco:CharacterString" />
                                </dif:Address>
                                <dif:City>
                                    <xsl:value-of select="gmd:identificationInfo/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:city/gco:CharacterString" />
                                </dif:City>
                                <dif:Province_or_State>
                                    <xsl:value-of select="gmd:identificationInfo/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:administrativeArea/gco:CharacterString" />
                                </dif:Province_or_State>
                                <dif:Postal_Code>
                                    <xsl:value-of select="gmd:identificationInfo/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:postalCode/gco:CharacterString" />
                                </dif:Postal_Code>
                                <dif:Country>
                                    <xsl:value-of select="gmd:identificationInfo/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:country/gco:CharacterString" />
                                </dif:Country>
                            </dif:Contact_Address>
                        </dif:Personel>
                    </dif:Data_Center>
                    <xsl:for-each select="//gmd:onLine">
                        <dif:Related_URL>
                            <dif:URL_Content_Type>
                                <dif:Type>
                                    <xsl:value-of select="normalize-space(substring-before(.//gmd:name/gco:CharacterString, '&gt;'))" />
                                </dif:Type>
                                <dif:SubType>
                                    <xsl:value-of select="normalize-space(substring-after(.//gmd:name/gco:CharacterString, '&gt;'))" />
                                </dif:SubType>
                            </dif:URL_Content_Type>
                            <dif:URL>
                                <xsl:value-of select=".//gmd:URL" />
                            </dif:URL>
                        </dif:Related_URL>
                    </xsl:for-each>
                    <dif:Summary>
                        <xsl:value-of select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract/gco:CharacterString" />
                    </dif:Summary>
                    <dif:IDN_Node>
                        <dif:Short_Name>NMDC</dif:Short_Name>
                    </dif:IDN_Node>
                    <dif:Metadata_Name>CEOS IDN DIF</dif:Metadata_Name>
                    <dif:Metadata_Version>9.7.1</dif:Metadata_Version>
                </dif:DIF>
            </nmdc:nmdc-metadata>
            <nmdc:parameters>
                <xsl:variable name="minLongitude" select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:westBoundLongitude/gco:Decimal" />
                <xsl:variable name="maxLongitude" select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:eastBoundLongitude/gco:Decimal" />
                <xsl:variable name="minLatitude" select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:southBoundLatitude/gco:Decimal" />
                <xsl:variable name="maxLatitude" select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:northBoundLatitude/gco:Decimal" />
                <xsl:if test="($minLongitude) and ($maxLongitude) and ($minLatitude) and ($maxLatitude)">
                    <xsl:choose>
                        <xsl:when test="abs($minLongitude - $maxLongitude) &gt; 0.1 and abs($minLatitude - $maxLatitude) &gt; 0.1">
                            <nmdc:polygon>POLYGON((<xsl:value-of select="$minLongitude"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$minLatitude"/>,<xsl:value-of select="$maxLongitude"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$minLatitude"/>,<xsl:value-of select="$maxLongitude"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$maxLatitude"/>,<xsl:value-of select="$minLongitude"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$maxLatitude"/>,<xsl:value-of select="$minLongitude"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$minLatitude" />))</nmdc:polygon>
                        </xsl:when>
                        <xsl:otherwise>
                            <nmdc:point>
                                <xsl:value-of select="$minLongitude"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$minLatitude"/>
                            </nmdc:point>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:if>
                <nmdc:pDefs>
                    <xsl:for-each select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:descriptiveKeywords">
                        <xsl:for-each select="./gmd:MD_Keywords">
                            <xsl:if test="not(//gmd:thesaurusName)or ./gmd:thesaurusName/gmd:CI_Citation/gmd:title/gco:CharacterString = 'NASA/GCMD Science Keywords'">
                                <xsl:for-each select="./gmd:keyword">
                                    <xsl:variable name="str" as="xs:string" select="./gco:CharacterString" />
                                    <xsl:variable name="delim" as="xs:string" select="'&gt;'" />
                                    <nmdc:pDef>EARTH SCIENCE<xsl:if test="string-length(normalize-space(tokenize($str,$delim)[1])) &gt; 0">&gt;
                                            <xsl:value-of select="normalize-space(replace(upper-case(normalize-space(tokenize($str,$delim)[1])), $delim, ''))" />
                                        </xsl:if>
                                        <xsl:if test="string-length(normalize-space(tokenize($str,$delim)[2])) &gt; 0" >&gt;
                                            <xsl:value-of select="normalize-space(replace(upper-case(normalize-space(tokenize($str,$delim)[2])), $delim, ''))" />
                                        </xsl:if>
                                        <xsl:if test="string-length(normalize-space(tokenize($str,$delim)[3])) &gt; 0" >&gt;
                                            <xsl:value-of select="normalize-space(replace(upper-case(normalize-space(tokenize($str,$delim)[3])), $delim, ''))" />
                                        </xsl:if>
                                    </nmdc:pDef>
                                </xsl:for-each>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:for-each>
                    <nmdc:identifer>{$identifier}</nmdc:identifer> 
                </nmdc:pDefs>

            </nmdc:parameters>

        </nmdc:meta>
    </xsl:template>

</xsl:stylesheet>