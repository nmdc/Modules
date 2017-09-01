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
    <xsl:param name="identifier" />

    <xsl:template match="dif:URL_Content_Type">
        <xsl:copy>
            <xsl:element name="Type" namespace="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">
                <xsl:value-of select="./dif:Type" />
            </xsl:element>
            <xsl:element name="Subtype" namespace="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">
                <xsl:value-of select="./dif:Subtype" />
            </xsl:element>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="dif:Parameters">
        <xsl:copy>
            <xsl:element name="Category" namespace="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">
                <xsl:value-of select="normalize-space(./dif:Category)" />
            </xsl:element>
            <xsl:element name="Topic" namespace="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">
                <xsl:value-of select="normalize-space(./dif:Topic)" />
            </xsl:element>
            <xsl:element name="Term" namespace="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">
                <xsl:value-of select="normalize-space(./dif:Term)" />
            </xsl:element>
            <xsl:element name="Variable_Level_1" namespace="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">
                <xsl:value-of select="normalize-space(./dif:Variable_Level_1)" />
            </xsl:element>
            <xsl:element name="Variable_Level_2" namespace="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">
                <xsl:value-of select="normalize-space(./dif:Variable_Level_2)" />
            </xsl:element>
            <xsl:element name="Variable_Level_3" namespace="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">
                <xsl:value-of select="normalize-space(./dif:Variable_Level_3)" />
            </xsl:element>

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
                        <xsl:when test="abs($minLongitude - $maxLongitude) &lt; 0.1 and abs($minLatitude - $maxLatitude) &lt; 0.1">
                            <nmdc:point>
                                <xsl:value-of select="$minLongitude"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$minLatitude"/>
                            </nmdc:point>
                        </xsl:when>
                        <xsl:otherwise>
                            <nmdc:polygon>POLYGON((<xsl:value-of select="$minLongitude - 0.1"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$minLatitude - 0.1"/>,<xsl:value-of select="$maxLongitude + 0.1"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$minLatitude - 0.1"/>,<xsl:value-of select="$maxLongitude + 0.1"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$maxLatitude + 0.1"/>,<xsl:value-of select="$minLongitude - 0.1"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$maxLatitude + 0.1"/>,<xsl:value-of select="$minLongitude - 0.1"/>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$minLatitude - 0.1" />))</nmdc:polygon>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:if>
                <nmdc:pDefs>
                    <xsl:for-each select="//dif:Parameters">
                        <nmdc:pDef>
                            <xsl:value-of select="normalize-space(./dif:Category)"/>
                            <xsl:if test="dif:Topic">&gt;<xsl:value-of select="normalize-space(dif:Topic)"/></xsl:if>
                            <xsl:if test="dif:Term">&gt;<xsl:value-of select="normalize-space(dif:Term)"/></xsl:if>
                            <xsl:if test="dif:Variable_Level_1">&gt;<xsl:value-of select="normalize-space(dif:Variable_Level_1)"/></xsl:if>
                            <xsl:if test="dif:Variable_Level_2">&gt;<xsl:value-of select="normalize-space(dif:Variable_Level_2)"/></xsl:if>
                            <xsl:if test="dif:Variable_Level_3">&gt;<xsl:value-of select="normalize-space(dif:Variable_Level_3)"/></xsl:if>
                        </nmdc:pDef>
                    </xsl:for-each>
                </nmdc:pDefs>
                <nmdc:identifer>
                    <xsl:value-of select="$identifier" />
                </nmdc:identifer> 
            </nmdc:parameters>
        </nmdc:meta>
    </xsl:template>

</xsl:stylesheet>