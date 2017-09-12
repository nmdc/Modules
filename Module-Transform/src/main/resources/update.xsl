<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:nmdc="http://www.nmdc.org/2015/01/Metadata"
                xmlns:dif="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/">

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" media-type="text/xml"/>
    <xsl:strip-space elements="*" />
    
    <xsl:template match="/">
        <xsl:element name="update">
        <xsl:attribute name="overwrite">true</xsl:attribute>
        <xsl:element name="add">
            <xsl:element name="doc">
                <xsl:for-each select="nmdc:meta">
                    <xsl:element name="field">
                        <xsl:attribute name="name">Entry_ID</xsl:attribute>
                        <xsl:value-of select="nmdc:nmdc-metadata/dif:DIF/dif:Entry_ID" />
                    </xsl:element>
                    <xsl:element name="field">
                        <xsl:attribute name="name">Entry_Title</xsl:attribute>
                        <xsl:value-of select="nmdc:nmdc-metadata/dif:DIF/dif:Entry_Title" />
                    </xsl:element>
                    <xsl:variable name="startDate" select="nmdc:nmdc-metadata/dif:DIF/dif:Temporal_Coverage/dif:Start_Date" />
                    <xsl:variable name="stopDate" select="nmdc:nmdc-metadata/dif:DIF/dif:Temporal_Coverage/dif:Stop_Date" />
                    <xsl:element name="field"><xsl:attribute name="name">Start_Date</xsl:attribute><xsl:value-of select="substring($startDate, 1, 10)" />T00:00:00Z</xsl:element>    
                    <xsl:element name="field"><xsl:attribute name="name">Stop_Date</xsl:attribute><xsl:value-of select="substring($stopDate, 1, 10)" />T00:00:00Z</xsl:element>   
                    <xsl:element name="field">
                        <xsl:attribute name="name">Scientific_Keyword</xsl:attribute>
                        <xsl:value-of select="nmdc:parameters/nmdc:pDefs/nmdc:pDef" />
                    </xsl:element>    
                    <xsl:element name="field">
                        <xsl:attribute name="name">Provider</xsl:attribute>
                        <xsl:value-of select="nmdc:nmdc-metadata/dif:DIF/dif:Originating_Center" />
                    </xsl:element>  
                    <xsl:element name="field">
                        <xsl:attribute name="name">Data_URL</xsl:attribute>
                        <xsl:value-of select="nmdc:nmdc-metadata/dif:DIF/dif:Related_URL/dif:URL" />
                    </xsl:element>     
                    <xsl:element name="field">
                        <xsl:attribute name="name">Data_URL_Type</xsl:attribute>
                        <xsl:value-of select="nmdc:nmdc-metadata/dif:DIF/dif:Related_URL/dif:URL_Content_Type/dif:Type" />
                    </xsl:element>   
                    <xsl:element name="field">
                        <xsl:attribute name="name">Data_URL_Subtype</xsl:attribute>
                        <xsl:value-of select="nmdc:nmdc-metadata/dif:DIF/dif:Related_URL/dif:URL_Content_Type/dif:SubType" />
                    </xsl:element>        
                    <xsl:element name="field">
                        <xsl:attribute name="name">Data_Summary</xsl:attribute>
                        <xsl:value-of select="nmdc:nmdc-metadata/dif:DIF/dif:Summary" />
                    </xsl:element>    
                    <xsl:element name="field">
                        <xsl:attribute name="name">PersonFirstName</xsl:attribute>
                        <xsl:value-of select="nmdc:nmdc-metadata/dif:DIF/dif:Data_Center/dif:Personnel/dif:First_Name" />
                    </xsl:element>   
                    <xsl:element name="field">
                        <xsl:attribute name="name">PersonLastName</xsl:attribute>
                        <xsl:value-of select="/nmdc:nmdc-metadata/dif:DIF/dif:Data_Center/dif:Personnel/dif:Last_Name" />
                    </xsl:element>   
                    <xsl:element name="field">
                        <xsl:attribute name="name">identifier</xsl:attribute>
                        <xsl:value-of select="nmdc:parameters/nmdc:identifer" />
                    </xsl:element>    
                    <xsl:if test="nmdc:parameters/nmdc:polygon">
                        <xsl:element name="field">
                            <xsl:attribute name="name">location_rpt</xsl:attribute>
                            <xsl:value-of select="nmdc:parameters/nmdc:polygon" />
                        </xsl:element> 
                    </xsl:if>
                    <xsl:if test="nmdc:parameters/nmdc:point">
                        <xsl:element name="field">
                            <xsl:attribute name="name">location_rpt</xsl:attribute>
                            <xsl:value-of select="nmdc:parameters/nmdc:point" />
                        </xsl:element> 
                    </xsl:if>                                                                                                                                                                                                                                                                   
                </xsl:for-each>
            </xsl:element>
        </xsl:element>
        </xsl:element>
    </xsl:template>
    
</xsl:stylesheet>