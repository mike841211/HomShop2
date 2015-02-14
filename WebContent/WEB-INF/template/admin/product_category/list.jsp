<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="../inc.header.jsp" />
</head>
<body>
<div class="mini-toolbar" style="border-bottom:0;">
	<a class="mini-button" plain="true" iconCls="icon-add" onclick="add()">增加</a>
	<a class="mini-button" plain="true" iconCls="icon-edit" onclick="edit()">修改</a>
	<a class="mini-button" plain="true" iconCls="icon-remove" onclick="dele()">删除</a>
	<span class="separator"></span>
	<a class="mini-button" plain="true" iconCls="icon-reload" onclick="refreshRootNode()">刷新</a>
	<a class="mini-button" plain="true" iconCls="icon-reload" onclick="refreshNode()">刷新节点</a>
</div>
<div class="mini-fit">
	<div id="treegrid1" class="mini-treegrid" style="width:100%;height:100%;"
		showTreeIcon="true" showCheckBox="true" checkRecursive="false" fitColumns="true"
		treeColumn="name" idField="id" parentField="pid" resultAsTree="false"
		url="getProductCategoryTreeData.do"
	>
		<div property="columns">
			<div type="indexcolumn" width="40" headerAlign="center">序号</div>
			<div field="displayorder" width="40" headerAlign="center" align="center">排序</div>
			<div field="inuse" width="40" headerAlign="center" align="center" renderer="onInuseRenderer">状态</div>
			<div name="name" field="name" width="300">名称</div>
			<div field="code" width="150">分类编号</div>
			<div field="id" width="100%">ID</div>
		</div>
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
			url: "product_category/add.htm",
			title: "新建商品分类",
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
				url: "product_category/edit.htm?id="+node.id,
				title: "编辑商品分类",
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
</script>
</body>
</html>
