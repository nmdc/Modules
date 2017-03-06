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
    <xsl:strip-space elements="*" />

    <xsl:template match="dif:URL_Content_Type">
        <xsl:copy>
<xsl:element name="Type" namespace="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">
<xsl:value-of select="./dif:Type" />
</xsl:element>
<xsl:element name="SubType" namespace="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">
           <xsl:value-of select="./dif:SubType" />
</xsl:element>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="dif:Category">
        <xsl:copy>
            <xsl:value-of select="normalize-space(.)" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="dif:Topic">
        <xsl:copy>
            <xsl:value-of select="normalize-space(.)" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="dif:Term">
        <xsl:copy>
            <xsl:value-of select="normalize-space(.)" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="dif:Variable_Level_1">
        <xsl:copy>
            <xsl:value-of select="normalize-space(.)" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="dif:Data_Center[1]">
        <xsl:copy>
            <xsl:copy-of select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="dif:DIF">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="dif:*">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()" />
        </xsl:copy>
    </xsl:template>

    <!-- MATCH ROOT DIF -->
    <xsl:template match="/">
        <nmdc:meta>
            <nmdc:nmdc-metadata>
                <xsl:copy>
                    <xsl:apply-templates select="dif:*"/>
                </xsl:copy>
            </nmdc:nmdc-metadata>
            <nmdc:parameters>
                <xsl:variable name="minLongitude" select="max(/dif:DIF/*/dif:Easternmost_Longitude)" />
                <xsl:variable name="maxLongitude" select="min(/dif:DIF/*/dif:Westernmost_Longitude)" />
                <xsl:variable name="minLatitude" select="min(/dif:DIF/*/dif:Southernmost_Latitude)" />
                <xsl:variable name="maxLatitude" select="max(/dif:DIF/*/dif:Northernmost_Latitude)" />
                <xsl:if test="($minLongitude) and ($maxLongitude) and ($minLatitude) and ($maxLatitude)">
                    <xsl:choose>
                        <xsl:when test="abs($minLongitude - $maxLongitude) &lt; 0.1 or abs($minLatitude - $maxLatitude) &lt; 0.1">
                            <nmdc:point>
                                <xsl:value-of select="$minLongitude"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$minLatitude"/>
                            </nmdc:point>
                        </xsl:when>
                        <xsl:otherwise>
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
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:if>
            </nmdc:parameters>
        </nmdc:meta>
    </xsl:template>

</xsl:stylesheet>