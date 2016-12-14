<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:nmdc="http://www.nmdc.org/2015/01/Metadata" xmlns:dif="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/" exclude-result-prefixes="nmdc dif">
  <xsl:output method="html" version="5.0" encoding="UTF-8" indent="yes"/>

  <xsl:template match="/nmdc:meta/nmdc:nmdc-metadata/dif:DIF" >


    <div class="box" id="title"><span class="label">Title</span><span class="value"><xsl:value-of select="dif:Entry_Title/text()"/></span></div>    

    <div class="loopBox" id="dataset_citation">
      <div class="loopLabel">Data Set Citation</div>
      <xsl:for-each select="dif:Data_Set_Citation"> 
        <div class="box" id="creator"><span class="label">Creator</span><span class="value"><xsl:value-of select="dif:Dataset_Creator/text()"/></span></div>
        <div class="box" id="title"><span class="label">Title</span><span class="value"><xsl:value-of select="dif:Dataset_Title/text()"/></span></div>
        <div class="box" id="releaseData"><span class="label">Release Data</span><span class="value"><xsl:value-of select="dif:Dataset_Release_Date/text()"/></span></div>
        <div class="box" id="releasePlace"><span class="label">Release Place</span><span class="value"><xsl:value-of select="dif:Dataset_Release_Place/text()"/></span></div>
        <div class="box" id="publisher"><span class="label">Publisher</span><span class="value"><xsl:value-of select="dif:Dataset_Publisher/text()"/></span></div>
        <div class="box" id="version"><span class="label">Version</span><span class="value"><xsl:value-of select="dif:Version/text()"/></span></div>
      </xsl:for-each>
    </div>

    <div id="personnel">
      <div class="loopLabel">Personnel</div>
      <xsl:for-each select="dif:Personnel">
        <div class="box" id="role"><span class="label">Role</span><span class="value"><xsl:value-of select="dif:Role/text()"/></span></div>
	<div class="box" id="firstName"><span class="label">First Name</span><span class="value"><xsl:value-of select="dif:First_Name/text()"/></span></div>
	<div class="box" id="lastName"><span class="label">Last Name</span><span class="value"><xsl:value-of select="dif:Last_Name/text()"/></span></div>
	<div class="box" id="email"><span class="label">Email</span><span class="value"><xsl:value-of select="dif:Email/text()"/></span></div>
	<div class="box" id="phone"><span class="label">Phone</span><span class="value"><xsl:value-of select="dif:Phone/text()"/></span></div>
	<div class="box" id="address"><span class="label">Contact Address</span><span class="value"><xsl:value-of select="dif:Contact_Address/dif:Address/text()"/></span></div>
	<div class="box" id="city"><span class="label">Contact City</span><span class="value"><xsl:value-of select="dif:Contact_Address/dif:City/text()"/></span></div>
	<div class="box" id="phone"><span class="label">Contact Phone</span><span class="value"><xsl:value-of select="dif:Contact_Address/dif:Phone/text()"/></span></div>
	<div class="box" id="country"><span class="label">Contact Country</span><span class="value"><xsl:value-of select="dif:Contact_Address/dif:Country/text()"/></span></div>
      </xsl:for-each>
    </div>

    <div id="parameters">
      <div class="loopLabel">Parameters</div>
      <xsl:for-each select="dif:Parameters">
        <div class="loopLabel" id="path"><span class="label">Parameter</span>
<span class="value">
<xsl:value-of select="dif:Category"/>/<xsl:value-of select="dif:Topic"/>/<xsl:value-of select="dif:Term"/>/<xsl:value-of select="dif:Variable_Level_1"/>/<xsl:value-of select="dif:Variable_Level_2"/>/><xsl:value-of select="dif:Variable_Level_3"/>
</span>
	</div>
      </xsl:for-each>
    </div>

    <div id="temporal_coverage">
      <div class="box" id="start"><span class="label">Start Date</span><span class="value"><xsl:value-of select="dif:Temporal_Coverage/dif:Start_Date/text()"/></span></div>
      <div class="box" id="stop"><span class="label">Stop Date</span><span class="value"><xsl:value-of select="dif:Temporal_Coverage/dif:Stop_Date/text()"/></span></div>
    </div>

    <div class="box" id="datasetProgress"><span class="label">Data Set Progress</span><span class="value"><xsl:value-of select="dif:Data_Set_Progress/text()"/></span></div>

    <div class="box" id="location"><span class="label">Detailed Location</span><span class="value"><xsl:value-of select="dif:Location/dif:Detailed_Location/text()"/></span></div>	
    <div class="box" id="projectShortname"><span class="label">Project Short Name</span><span class="value"><xsl:value-of select="dif:Project/dif:Short_Name/text()"/></span></div>
    <div class="box" id="projectLongname"><span class="label">Project Long Name</span><span class="value"><xsl:value-of select="dif:Project/dif:Long_Name/text()"/></span></div>

    <div class="box" id="location"><span class="label">Detailed Location</span><span class="value"><xsl:value-of select="dif:Location/dif:Detailed_Location/text()"/></span></div>

    <div id="datacenter">
      <div class="box" id="datacenterShortname"><span class="label">Data Center Short Name</span><span class="value"><xsl:value-of select="dif:Data_Center/dif:Data_Center_Name/Short_Name/text()"/></span></div>
      <div class="box" id="datacenterLongname"><span class="label">Data Center Long Name</span><span class="value"><xsl:value-of select="dif:Data_Center/dif:Data_Center_Name/Long_Name/text()"/></span></div>
      <div class="box" id="datacenterUrl"><span class="label">Data Center URL</span><span class="value"><xsl:value-of select="dif:Data_Center/dif:Data_Center_URL/text()"/></span></div>
      <div id="personnel">
	<div class="loopLabel">Personnel</div>
        <xsl:for-each select="dif:Data_Center/dif:Personnel">
          <div class="box" id="Role"><span class="label">Role</span><span class="value"><xsl:value-of select="dif:Role"/></span></div>
          <div class="box" id="firstName"><span class="label">First Name</span><span class="value"><xsl:value-of select="dif:First_Name/text()"/></span></div>
          <div class="box" id="lastName"><span class="label">Last Name</span><span class="value"><xsl:value-of select="dif:Last_Name/text()"/></span></div>
          <div class="box" id="email"><span class="label">Email</span><span class="value"><xsl:value-of select="dif:Email/text()"/></span></div>
          <div class="box" id="phone"><span class="label">Phone</span><span class="value"><xsl:value-of select="dif:Phone/text()"/></span></div>
          <div class="box" id="address"><span class="label">Contact Address</span><span class="value"><xsl:value-of select="dif:Contact_Address/Address/text()"/></span></div>
          <div class="box" id="city"><span class="label">Contact City</span><span class="value"><xsl:value-of select="dif:Contact_Address/City/text()"/></span></div>
  	  <div class="box" id="phone"><span class="label">Contact Phone</span><span class="value"><xsl:value-of select="dif:Contact_Address/Phone/text()"/></span></div>
          <div class="box" id="country"><span class="label">Contact Country</span><span class="value"><xsl:value-of select="dif:Contact_Address/Country/text()"/></span></div>
        </xsl:for-each>
      </div>
    </div>

    <div class="box" id="summary"><span class="label">Abstract</span><span class="value"><xsl:value-of select="dif:Summary/dif:Abstract/text()"/></span></div>
    <div id="relatedUrl">
      <div class="loopLabel"/>
      <xsl:for-each select="dif:Related_URL">
        <div class="box" id="url"><span class="label">Url</span><span class="value"><xsl:value-of select="dif:URL"/></span></div>
      </xsl:for-each>
    </div>



  </xsl:template>
<xsl:template match="/nmdc:meta/nmdc:parameters">
  <div id="bbox" wkt="{nmdc:polygon}" />
</xsl:template>

</xsl:stylesheet>


























