<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:nmdc="http://www.nmdc.org/2015/01/Metadata" xmlns:dif="http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/"  exclude-result-prefixes="nmdc dif">
  <xsl:output method="html" version="5.0" encoding="UTF-8" indent="yes"/>
  
  <xsl:template match="/nmdc:meta/nmdc:nmdc-metadata/dif:DIF" >
    
    <div id="Entry_ID"><xsl:value-of select="dif:Entry_ID/text()"/></div>
    <div id="Entry_Title"><xsl:value-of select="dif:Entry_Title/text()"/></div>

    <div id='Data_Set_Citation_List'>
      <xsl:for-each select="dif:Data_Set_Citation">
	<div class='Data_Set_Citation'>
	  <xsl:for-each select="child::*">
	    <div class="{local-name()}">
	      <xsl:value-of select="text()"/>
	    </div>
	  </xsl:for-each>
	</div>
      </xsl:for-each>
    </div> 

    <div id='Personnel_List'>
      <xsl:for-each select="dif:Personnel">
	<div class='Personnel'>
	  <xsl:for-each select="child::*">
	    <div class="{local-name()}">
	      <xsl:value-of select="text()"/>
	    </div>
	  </xsl:for-each>
	</div>
      </xsl:for-each>
    </div>
    
    <div id='Parameters_List'>
      <xsl:for-each select="dif:Parameters">
	<div class='Parameters'>
	  <xsl:for-each select="child::*">
	    <div class="{local-name()}">
	      <xsl:value-of select="text()"/>
	    </div>
	  </xsl:for-each>
	</div>
      </xsl:for-each>
    </div>

    <div id='ISO_Topic_Category_List'>
      <xsl:for-each select="dif:ISO_Topic_Category">
	<div class='ISO_Topic_Category'>
	  <xsl:value-of select="text()"/>
	</div>
      </xsl:for-each>
    </div>
    

    <div id='Keywords_List'>
      <xsl:for-each select="dif:Keyword">
	<div class='Keyword'>
	  <xsl:value-of select="text()"/>
	</div>
      </xsl:for-each>
    </div>

    <div id="Temporal_Coverage">
      <div class="Start_Date"><xsl:value-of select="dif:Temporal_Coverage/dif:Start_Date/text()"/></div>
      <div class="Stop_Date"><xsl:value-of select="dif:Temporal_Coverage/dif:Stop_Date/text()"/></div>
    </div>

    <div id="Data_Set_Progress"><xsl:value-of select="dif:Data_Set_Progress/text()"/></div>
    
    <div id="Spatial_Coverage">
      <xsl:for-each select="dif:Spatial_Coverage/child::*">
	<div class="{local-name()}">
	  <xsl:value-of select="text()"/>
	</div>
      </xsl:for-each>
    </div>
    
    <div id='Location_List'>
      <xsl:for-each select="dif:Location">
	<div class='Location'>
	  <xsl:for-each select="child::*">
	    <div class="{local-name()}">
	      <xsl:value-of select="text()"/>
	    </div>
	  </xsl:for-each>
	</div>
      </xsl:for-each>
    </div>

    <div id='Project_List'>
      <xsl:for-each select="dif:Project">
	<div class='Project'>
	  <xsl:for-each select="child::*">
	    <div class="{local-name()}">
	      <xsl:value-of select="text()"/>
	    </div>
	  </xsl:for-each>
	</div>
      </xsl:for-each>
    </div>
    
    <div id="Access_Constraints"><xsl:value-of select="dif:Access_Constraints/text()"/></div>
    <div id="Originating_Center"><xsl:value-of select="dif:Originating_Center/text()"/></div>
                                                                 
    
    <div id='Data_Center_List'>
      <xsl:for-each select="dif:Data_Center">
	<div class='Data_Center'>
	  <xsl:for-each select="child::*">
	    <div class="{local-name()}">
	      <xsl:choose>
		<xsl:when test="*">
		  <xsl:for-each select="child::*">
		    <div class="{local-name()}">
		      <xsl:value-of select="text()"/>
		    </div>  
		  </xsl:for-each>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:value-of select="text()"/>
		</xsl:otherwise>
	      </xsl:choose>
	    </div>
	  </xsl:for-each>
	</div>
      </xsl:for-each>
    </div>

    <div id='Distribution_List'>
      <xsl:for-each select="dif:Distribution">
	<div class='Project'>
	  <xsl:for-each select="child::*">
	    <div class="{local-name()}">
	      <xsl:value-of select="text()"/>
	    </div>
	  </xsl:for-each>
	</div>
      </xsl:for-each>
    </div>

    <div id='Reference_List'>
      <xsl:for-each select="dif:Reference">
	<div class='Reference'>
	  <xsl:value-of select="text()"/>
	</div>
      </xsl:for-each>
    </div>

    <div id="Summary"><xsl:value-of select="dif:Summary/text()"/></div>

    <div id='Related_URL_List'>
      <xsl:for-each select="dif:Related_URL">
	<div class='Related_URL'>
	  <xsl:for-each select="child::*">
	    <div class="{local-name()}">
	      <xsl:choose>
		<xsl:when test="*">
		  <xsl:for-each select="child::*">
		    <div class="{local-name()}">
		      <xsl:value-of select="text()"/>
		    </div>  
		  </xsl:for-each>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:value-of select="text()"/>
		</xsl:otherwise>
	      </xsl:choose>
	    </div>
	  </xsl:for-each>
	</div>
      </xsl:for-each>
    </div>

    <div id="Originating_Metadata_Node"><xsl:value-of select="dif:Originating_Metadata_Node/text()"/></div>
    <div id="Metadata_Name"><xsl:value-of select="dif:Metadata_Name/text()"/></div>
    <div id="Metadata_Version"><xsl:value-of select="dif:Metadata_Version/text()"/></div>
    <div id="DIF_Creation_Date"><xsl:value-of select="dif:DIF_Creation_Date/text()"/></div>

    <div id="Use_Constraints"><xsl:value-of select="dif:Use_Constraints/text()"/></div>
    
  </xsl:template>


  <xsl:template match="/nmdc:meta/nmdc:parameters">
    <div id="boundingboxWKT"><xsl:value-of select="nmdc:polygon"/></div>

  </xsl:template>

  

</xsl:stylesheet>
    

