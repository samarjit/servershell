<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title></title>
	<style type="text/css">
	.odd{
		background-color: lightgrey
	}
	.even{
		background-color: red
	}
	table{
		border-collapse: collapse;
		
	}
	td{
	border-right: 1px dotted black;
	}
	</style>
	
</head>
<body>

<table>
  <tr>
<#list headers  as col>
    <th>${col}</th>
</#list>
  </tr>
 <#assign counter="even"> 
 
 
<#list myrows as rw>
<#if counter == "even">
  <tr class="${counter}">
  <#assign counter="odd">
<#else>
  <tr class="${counter}">
  <#assign counter="even">
</#if>   

  
  <td>  ${rw.cardNO} </td> 
  <td>   ${rw.reportTime}</td>  
  <td>   ${rw.BlockTime}</td>  
  <td>   ${rw.refundStatus}</td>  
  <td>   ${rw.SentFileName}</td>  
  <td>   ${rw.SentFileTime}</td>  
  <td>   ${rw.ReceiveFileName}</td>  
  <td>   ${rw.ReceiveFileTime}</td>  
  <td>   ${rw.FileRefId}</td>  
  </tr>
</#list>

<#list rowmodel  as rmodel>
    ${rmodel},
</#list>

</table>
 


</body>
</html> 