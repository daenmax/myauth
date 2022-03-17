<!--
MyAuth updateLog
By Daen
QQ1330166565
 -->
 <?php
/**
* 更新日志
* Daen
* QQ 1330166565
*
* 部署更新日志后，域名 + ?skey=软件KEY 即可
**/
//服务器地址
$server_url = "http://localhost:8080/myauth/";
//错误跳转地址
$error_url = "http://mycdn.daenx.cn/error.html";
$skey = $_GET['skey'];
if($skey == ''){
	redirect($error_url);
}
$ret = file_get_contents($server_url."web/getUpdateLog?skey=".$skey);
$json = json_decode($ret,true);
if( $json['code'] != 200){
	redirect($error_url);
}
$updlogList = $json['result']['updlogList'];
function replaceFlag($updLog){
	$ret = str_replace('【新增】','<span class="layui-badge layui-bg-green">新增</span> &nbsp;&nbsp;',$updLog);
	$ret = str_replace('【修复】','<span class="layui-badge layui-bg-orange">修复</span> &nbsp;&nbsp;',$ret);
	$ret = str_replace('【优化】','<span class="layui-badge layui-bg-blue">优化</span> &nbsp;&nbsp;',$ret);
	$ret = str_replace('【其他】','<span class="layui-badge layui-bg-black">其他</span> &nbsp;&nbsp;',$ret);
	$ret = str_replace('【删除】','<span class="layui-badge layui-bg-red">删除</span> &nbsp;&nbsp;',$ret);
	return $ret;
}
function replaceHr($updLog){
	return str_replace(array("\r\n","\n","\r"),'<br/>',$updLog);
}
function redirect($url){
    header("Location: $url");
    exit();
}
function replaceTime($timestamp){
    return date('Y-m-d H:i:s',$timestamp);
}
?>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title><?=$json['result']['softName']?> - 更新日志</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="css/layui.css" media="all">
  <link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
</head>
<body style="padding:20px;max-width:700px;margin:0 auto;">
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
  <legend>๑乛◡乛๑卡在了奇怪的地方</legend>
</fieldset>
<center>
<a href="#"><button type="button" class="layui-btn layui-btn-normal layui-btn-radius"><?=$json['result']['softName']?></button></a><br>
</center>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
  <legend>更新日志</legend>
</fieldset>
<ul class="layui-timeline">
<?php
$huo = '';
$yuan = '';
$emoji = '💗';
$xiao = '😃';
$flag = $xiao;
foreach($updlogList as $updlog){
	echo '<li class="layui-timeline-item"><i class="layui-icon layui-timeline-axis">'.$flag.'</i><div class="layui-timeline-content layui-text"><h3 class="layui-timeline-title">Ver:';
	echo $updlog['ver'].'&nbsp;&nbsp;'.replaceTime($updlog['upd_time']).'</h3>'.replaceHr(replaceFlag($updlog['upd_log'])).'</div></li>';
}
?>
</ul>
<script src="js/layui.js" charset="utf-8"></script>
</body>
</html>
