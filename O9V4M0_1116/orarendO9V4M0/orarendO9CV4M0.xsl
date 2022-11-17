<?xml version = "1.0" encoding = "UTF-8"?>

<xsl:stylesheet version = "1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match = "/">
    <html>
      <body>
		<h2> Orarend </h2>
        
		<table border="4">
	  	<tr bgcolor = "#9acd32">
	    	<th>ID</th>
	    	<th>Tárgy</th>
	    	<th>nap</th>
	    	<th>tól</th>
	    	<th>ig</th>
	    	<th>helyszín</th>
	    	<th>oktató</th>
	    	<th>szak</th>
	  	</tr>

	  	<xsl:for-each select="JMT_orarend/ora">
	    	<tr>
	      		<td><xsl:value-of select = "@id"/></td>
	      		<td><xsl:value-of select = "targy"/></td>
				<xsl:for-each select="idopont">
					<td><xsl:value-of select = "nap"/></td>
					<td><xsl:value-of select = "tol"/></td>
					<td><xsl:value-of select = "ig"/></td>
				</xsl:for-each>
	      		<td><xsl:value-of select = "helyszin"/></td>
	      		<td><xsl:value-of select = "oktato"/></td>
	      		<td><xsl:value-of select = "szak"/></td>
	    	</tr>
	  	</xsl:for-each>
		</table>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>