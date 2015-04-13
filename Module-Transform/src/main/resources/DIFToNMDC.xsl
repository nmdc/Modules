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
                xmlns:nmdc="http://www.nmdc.org/2015/01/Metadata"
                xmlns:dif="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" media-type="text/xml"/>
    <xsl:preserve-space elements="nmdc:polygon" />

    <!-- MATCH ROOT DIF -->
    <xsl:template match="dif:DIF">
        <nmdc:meta>
            <nmdc:nmdc-metadata>
                <xsl:copy-of select="." />
            </nmdc:nmdc-metadata>
            <nmdc:parameters>
                <nmdc:polygon>
                    <xsl:variable name="minLongitude" select="//dif:Easternmost_Longitude" />
                    <xsl:variable name="maxLongitude" select="//dif:Westernmost_Longitude" />
                    <xsl:variable name="minLatitude" select="//dif:Southernmost_Latitude" />
                    <xsl:variable name="maxLatitude" select="//dif:Northernmost_Latitude" />
                    POLYGON((<xsl:value-of select="$minLongitude"/>&#160;<xsl:value-of select="$minLatitude"/>,<xsl:value-of select="$maxLongitude"/>&#160;<xsl:value-of select="$minLatitude"/>,<xsl:value-of select="$maxLongitude"/>&#160;<xsl:value-of select="$maxLatitude"/>,<xsl:value-of select="$minLongitude"/>&#160;<xsl:value-of select="$maxLatitude"/>,<xsl:value-of select="$minLongitude"/>&#160;<xsl:value-of select="$minLatitude" />))
                </nmdc:polygon>
            </nmdc:parameters>
        </nmdc:meta>
    </xsl:template>
    <!-- ====================================================== -->

</xsl:stylesheet>