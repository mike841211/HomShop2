<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../../inc.header.jsp" />
</head>
<body>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-add" onclick="add()">增加</a>
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="edit()">修改</a>
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<span class="separator"></span>
	<a class="mini-button" plain="true" iconCls="icon-reload" onclick="refreshRootNode()">刷新</a>
	<a class="mini-button" plain="true" iconCls="icon-reload" onclick="refreshNode()">刷新节点</a>
	<span class="separator"></span>
	<a class="mini-button" plain="true" iconCls="icon-reload" onclick="updateMenuToServer()">更新微信菜单</a>
	<a class="mini-button" plain="true" iconCls="icon-tip" onclick="showServerMenus()">查看当前菜单</a>
</div>
<div class="mini-fit">
	<div id="treegrid1" class="mini-treegrid" style="width:100%;height:100%;"
		showTreeIcon="true" showCheckBox="true" checkRecursive="false" fitColumns="true"
		treeColumn="name" idField="id" parentField="pid" resultAsTree="false"
		url="getTreeData.do"
	>
		<div property="columns">
			<div type="indexcolumn" width="40" headerAlign="center">序号</div>
			<div field="displayorder" width="40" headerAlign="center" align="center">排序</div>
			<div field="inuse" width="40" headerAlign="center" align="center" renderer="onInuseRenderer">状态</div>
			<div name="name" field="name" width="250">名称</div>
			<div field="menuType" width="80">菜单类型</div>
			<div field="messageType" width="80">消息类型</div>
			<div field="url" width="100%">链接地址</div>
			<div field="id" width="250">ID</div>
		</div>
	</div>
</div>

<div id="window1" class="mini-window" title="当前菜单" style="width:90%;height:90%;"
    showModal="true" allowResize="true" allowDrag="true"
    >
	<div class="mini-fit">
		<div id="treegrid2" class="mini-treegrid" style="width:100%;height:100%;"
			showTreeIcon="true" fitColumns="true" expandOnLoad="true"
			treeColumn="name" idField="id" parentField="pid" resultAsTree="false"
		>
			<div property="columns">
				<div type="indexcolumn" width="40" headerAlign="center">序号</div>
				<div name="name" field="name" width="250">名称</div>
				<div field="menuType" width="80">菜单类型</div>
				<div field="url" width="100%">链接地址</div>
			</div>
		</div>
	</div>
	<div class="mini-toolbar" style="text-align:center;padding:5px;border-width:1px 0 0;">
		<a class="mini-button" onclick="getMenuFromServer()" style="width:60px;margin-right:20px;">刷新</a>
		<a class="mini-button" onclick="window1.hide()" style="width:60px;">取消</a>
	</div>
</div>

<script type="text/javascript">
	mini.parse();

	function onInuseRenderer(e) {
		return e.record.inuse=='1' ? '' : '<span style="color:red;">停用</span>';
	}

	var tree = mini.get("treegrid1");

    function refreshRootNode() {
		tree.load(tree.url);
    }

    function refreshNode() {
		var node = tree.getSelectedNode();
		if (node) {
			tree.loadNode(node);
		}else{
			refreshRootNode();
		}
    }

    function refreshParentNode() {
		var node = tree.getParentNode(tree.getSelectedNode());
        if (node==tree.getRootNode()) {
			refreshRootNode();
        }else{
           tree.loadNode(node);
		}
    }

	var MenuType= {"click":"消息","view":"链接"};
	var MessageType= {"text":"文本","news":"图文","music":"音乐"};
	tree.on("drawcell",function(e){
		if (e.field == "menuType") {
			if (e.record.isLeaf){
				if (e.value==''){
					e.cellHtml = '<span style="color:red;">(错误：未指定菜单类型)</span>';
				}else{
					e.cellHtml = MenuType[e.value];
				}
				return;
			}
			e.cellHtml = "";
		}else if (e.field == "messageType") {
			if (e.record.isLeaf){
				if (e.record.menuType=='click'){
					if (e.value==''){
						e.cellHtml = '<span style="color:red;">(错误：未指定消息类型)</span>';
					}else {
						e.cellHtml = MessageType[e.value];
					}
				}else if (e.record.menuType=='view'){
					e.cellHtml = '<span style="color:blue;">(链接)</span>';
				}
				return;
			}
			e.cellHtml = "";
		}else if (e.field == "url") {
			if (e.record.isLeaf){
				if (e.record.menuType=='view'){
					if (e.value==''){
						e.cellHtml = '<span style="color:red;">(错误：未指定链接地址)</span>';
						return;
					}
				}
			}
		}
	});

	//tree.on('nodecheck',function(e){
		//tree.selectNode(e.node);
	//});

	tree.on('nodeselect',function(e){
		tree.checkNode(e.node);
	});

	tree.on('preload',function(e){
		var expandOnLoad = e.sender.expandOnLoad;
		var nodes = e.data;
		for (var i=0; i<nodes.length; i++){
			nodes[i].expanded = expandOnLoad;
		}
	});

	function add() {
		miniExt.win.open({
			url: "weixin/menu/add.htm",
			title: "新建微信菜单",
			width: 500,
			height: 350
		}).callback(function(){
			refreshNode();
		});
	}
	function edit() {
        var node = tree.getSelectedNode();
        if (node) {
			miniExt.win.open({
				url: "weixin/menu/edit.htm?id="+node.id,
				title: "编辑微信菜单",
				width: 500,
				height: 350
			}).callback(function(){
				refreshParentNode();
			});
		} else {
			alert("请选中一条记录");
		}
	}
	function dele() {
		// console.log(tree.getCheckedNodes(),tree.getValue ( ));
		var checkedNodes = tree.getCheckedNodes();
		var value = tree.getValue();
		if (checkedNodes.length>0) {
			if (confirm("确定删除选中记录？")) {
				$.ajax({
					type: "POST",
					url: "delete.do",
					dataType: "json",
					data: {ids:value},
					success: function (json) {
						if (json.status=="success"){
							tree.removeNodes(checkedNodes);
						}else{
							alert(json.message);
						}
					}
				});
			}
		} else {
			alert("请选中一条记录");
		}
	}
	//	function search() {
	//		alert();
	////		var params={};
	////		var searchby = mini.get("searchby").getFormValue();
	////		var searchkey = mini.get("searchkey").getFormValue();
	////		params.searchby = searchby;
	////		params.searchkey= searchkey;
	////		grid.load(params);
	//            var key = mini.get("searchkey").getValue();
	//            if (key == "") {
	//                tree.clearFilter();
	//            } else {
	//                key = key.toLowerCase();
	//                tree.filter(function (node) {
	//                    var text = node.text ? node.text.toLowerCase() : "";
	//                    if (text.indexOf(key) != -1) {
	//                        return true;
	//                    }
	//                });
	//            }
	//	}
	function updateMenuToServer(){
		if (confirm("更新微信菜单？")) {
			$.ajax({
				type: "POST",
				url: "updateMenuToServer.do",
				dataType: "json",
				success: function (json) {
					if (json.status=="success"){
						top.miniExt.showMsg("更新已提交到服务器...");
					}else{
						alert(json.message);
					}
				}
			});
		}
	}


	var window1 = mini.get("window1");
	var tree2 = mini.get("treegrid2");
	tree2.on('update',function(e){
		e.sender.setBorderStyle("border:0;")
	});
	tree2.on("drawcell",function(e){
		if (e.field == "menuType") {
			if (e.record.isLeaf){
				if (e.value==''){
					e.cellHtml = '<span style="color:red;">(错误：未指定菜单类型)</span>';
				}else{
					e.cellHtml = MenuType[e.value];
				}
				return;
			}
			e.cellHtml = "";
		}
	});
	function showServerMenus(){
		window1.show();
		getMenuFromServer();
	}
	function tree2LoadDate(json){
		try{
			var data = [];
			var buttons = json.menu.button;
			for (var i=0; i<buttons.length; i++){
				var button = {};
				//button.pid = '1';
				button.id = 'b'+i;
				button.name = buttons[i].name;
				if (buttons[i].sub_button.length>0){
					button.isLeaf = false;
					var children=[];
					for (var j=0; j<buttons[i].sub_button.length; j++){
						var sub_button = {};
						sub_button.isLeaf = true;
						//sub_button.pid = button.id;
						sub_button.id = 'sb'+j;
						sub_button.name = buttons[i].sub_button[j].name;
						sub_button.menuType = buttons[i].sub_button[j].type;
						if (sub_button.menuType=="view"){
						sub_button.url = buttons[i].sub_button[j].url;
						}else{
						sub_button.url = buttons[i].sub_button[j].key;
						}
						children.push(sub_button);
					}
					button.children = children;
				}else{
					button.isLeaf = true;
					button.menuType = buttons[i].type;
					if (button.menuType=="view"){
					button.url = buttons[i].url;
					}else{
					button.url = buttons[i].key;
					}
				}
				data.push(button);
			}
			tree2.setData(data);
		console.debug(json);
		console.debug(data);
		}catch(e){
			alert('读取数据错误');
		}
	}
	function getMenuFromServer(){
		$.ajax({
			type: "POST",
			url: "getMenuFromServer.do",
			dataType: "json",
			success: function (json) {
				tree2LoadDate(json);
			}
		});
	}

</script>
</body>
</html>
