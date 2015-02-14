<!DOCTYPE html />
<html>
<head>
<title>HomShop管理中心</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<script src="${base}/resources/admin/js/boot.js" type="text/javascript"></script>
<script src="${base}/resources/common/artDialog/artDialog.js?skin=default"></script>
<script src="${base}/resources/common/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
(function (config) {
    config['lock'] = true;
    config['fixed'] = true;
    config['background'] = '#000';
    config['opacity'] = '0.1';
})(art.dialog.defaults);
</script>

<style type="text/css">
body
{
    padding-left:10px;
    font-size:13px;
}
h1
{
    font-size:20px;
    font-family:Verdana;
}
h4
{
    font-size:16px;
    margin-top:25px;
    margin-bottom:10px;
}

.description
{
    padding-bottom:30px;
    font-family:Verdana;
}
.description h3
{
    color:#CC0000;
    font-size:16px;
    margin:0 30px 10px 0px;
    padding:45px 0 8px;
    /*background:url(titleback.png) no-repeat scroll left bottom transparent*/
    border-bottom:solid 1px #888;
}


	body{
        margin:0;padding:0;border:0;width:100%;height:100%;overflow:hidden;
    }
    .header
    {
        background:url(${base}/resources/admin/images/header.gif) repeat-x 0 -1px;
    }
    .Note
    {
        background:url(${base}/resources/admin/images/Notes_Large.png) no-repeat;width:32px;height:32px;
    }
    .Reports
    {
        background:url(${base}/resources/admin/images/Reports_Large.png) no-repeat;width:32px;height:32px;
    }
    </style>
</head>
<body>

<div id="layout1" class="mini-layout" style="width:100%;height:100%;">
	<div class="header" height="70" region="north" showSplit="false" showHeader="false" allowResize="false">
		<h1 style="margin:0;padding:15px;cursor:default;font-family:'Trebuchet MS',Arial,sans-serif;">管理后台</h1>
        <div style="position:absolute;top:18px;right:10px;">
            <a class="mini-button mini-button-iconTop" iconCls="icon-add" plain="true" href="${base}/" target="_blank" >首页</a>
            <a class="mini-button mini-button-iconTop" iconCls="icon-user"  plain="true" onclick="updateMyInfo" >个人资料</a>
            <a class="mini-button mini-button-iconTop" iconCls="icon-close" plain="true" href="logout.do" >退出</a>
        </div>
	</div>
	<div title="south" height="30" region="south" showSplit="false" showHeader="false" allowResize="false">
		<div style="line-height:28px;text-align:center;cursor:default">Copyright © 版权所有 </div>
	</div>
	<div title="center" region="center" style="border:0;" bodyStyle="overflow:hidden;">
		<!--Splitter-->
		<div id="bb" class="mini-splitter" style="width:100%;height:100%;" borderStyle="border:0;">
			<div size="180" style="border:0;" maxSize="250" minSize="100" showCollapseButton="true">
				<!--OutlookMenu-->
				<div class="mini-outlookmenu" onitemclick="onItemClick" idField="id" parentField="pid" textField="text"
					url="${base}/admin/menudata.do">
				</div>
			</div>
			<div showCollapseButton="false" style="border:0;">
				<!--Tabs-->
				<div id="mainTabs" class="mini-tabs" activeIndex="2" style="width:100%;height:100%;">
					<div title="首页" >
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- <script src="http://code.jquery.com/ui/1.10.1/jquery-ui.js"></script> -->
    <script type="text/javascript">
        mini.parse();

        function showTab(node) {
            var tabs = mini.get("mainTabs");
            var id = "tab$" + node.id;
            var tab = tabs.getTab(id);
            if (!tab) {
                tab = {};
                tab.name = id;
                tab.title = node.text;
                tab.showCloseButton = true;
                tab.url = node.url;
                tabs.addTab(tab);
            }
            tabs.activeTab(tab);
//	//http://api.jqueryui.com/sortable/
//	$( ".mini-tabs-headers tr" ).sortable({ axis: 'x',
//		stop: function( event, ui ) {
//		console.log(arguments);
//		var prev = $(ui.item).prev();
//		if (prev.is('.mini-tabs-space2')){
//			prev.insertBefore($(ui.item));
//		}
//		console.log($(ui.item).prev().is('.mini-tabs-space2'));
//	} });
        }
		function onItemClick(e) {
            var item = e.item;
            showTab(item);
        }
	function updateMyInfo() {
		miniExt.win.open({
			url: "admin/myInfo.htm",
			title: "个人信息",
			width: 500,
			height: 400
		});
	}
</script>

</body>
</html>