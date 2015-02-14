$(function() {
	$(".input").each(function(){
		$(this).bind("keyup", function(){
			this.value = this.value.replace(/([^\d.])|([.]+[\d]*[.])/g,'');
		})
	});

	$(".add").bind('click',function(){
		var row = $('#tbl_template').find('tr').eq(0).clone();
		$('#J_details_table').append(row);
		if ($('#J_details_table').find('tr').size()>1){
			$('#J_details').show();
		}
	});

	$(document).on("click",".edit", function() {
		hideProvince();
		var activeRow = $(this).closest('tr');
		activeRow.addClass('active');

		if (!province){
			alert("没有省份数据");return;
		}

		var ul = $('#J_province').find('ul');
		ul.empty();
		var J_row_areaCode = $('.active .J_row_areaCode').val().split(',');
		var J_all_areaCode = $('#J_all_areaCode').val().split(',');
		var checked, disabled;
		for (var i in province){
			checked = '';
			disabled = '';
			for (j=0;j<J_row_areaCode.length;j++){
				if (J_row_areaCode[j]==i){checked = 'checked';break;}
			}
			if (checked==''){
				for (j=0;j<J_all_areaCode.length;j++){
					if (J_all_areaCode[j]==i){disabled = 'disabled';break;}
				}
			}
			ul.append('<li><label title="'+province[i]+'"><input type="checkbox" value="'+ i +'" '+checked+' '+disabled+' />'+province[i]+'</label></li>');
		}
		activeRow.find('.area').hide();
		activeRow.find('.allarea').html($('#J_province').html()).show();
		activeRow.find('.edit_row').hide();
	});

	$(document).on("click",".btn_cancel", function() {
		hideProvince();
	});

	$(document).on("click",".btn_select", function() {
		var new_select_code = [];
		var ul = $('.active :checkbox').each(function(){
			if (this.checked){
				new_select_code.push(this.value);
			}
		});

		var J_all_areaCode = $('#J_all_areaCode').val().split(',');
		var J_row_areaCode = $('.active .J_row_areaCode').val().split(',');
		for(i=0;i<J_all_areaCode.length;i++){
			if(J_row_areaCode[0]==J_all_areaCode[i]){
				J_all_areaCode.splice(i,J_row_areaCode.length);
				break;
			}
		}
		J_all_areaCode = J_all_areaCode.concat(new_select_code);
		J_row_areaCode = new_select_code;

		var J_row_areaName = [];
		for (var i=0,l=J_row_areaCode.length; i<l; i++){
			if (!J_row_areaCode[i]){continue;}
			J_row_areaName.push(province[J_row_areaCode[i]].substr(0,2));
		}
		$('.active .J_row_areaCode').val(J_row_areaCode.join(','));
		$('.active .J_row_areaName').val(J_row_areaName.join(','));
		if (J_row_areaName.length>0){
			$('.active .area').html(J_row_areaName.join(','));
		}else{
			$('.active .area').html('未添加地区');
		}

		var J_all_areaName = [];
		for (var i=0,l=J_all_areaCode.length; i<l; i++){
			J_all_areaName.push(province[J_all_areaCode[i]].substr(0,2));
		}
		$('#J_all_areaCode').val(J_all_areaCode.join(','));
		$('#J_all_areaName').val(J_all_areaName.join(','));

		hideProvince();
	});

	function hideProvince(){
		$('.active').each(function(){
			$(this).removeClass('active');
			$(this).find('.allarea').hide();
			$(this).find('.area').show();
			$(this).find('.edit_row').show();
		})
	}

	$(document).on("click",".delete", function() {
		if (confirm('您确定要删除当前地区的设置吗？')){
			hideProvince();
			var J_all_areaCode = $('#J_all_areaCode').val().split(',');
			var J_all_areaName = $('#J_all_areaName').val().split(',');
			var J_row_areaCode = $(this).closest('tr').find('.J_row_areaCode').val().split(',');
			for(i=0; i<J_all_areaCode.length; i++){
				if(J_row_areaCode[0]==J_all_areaCode[i]){
					J_all_areaCode.splice(i,J_row_areaCode.length);
					J_all_areaName.splice(i,J_row_areaCode.length);
					break;
				}
			}
			$('#J_all_areaCode').val(J_all_areaCode.join(','));
			$('#J_all_areaName').val(J_all_areaName.join(','));

			$(this).closest('tr').remove();
			if ($('#J_details_table').find('tr').size()<=1){
				$('#J_details').hide();
			}
		}
	});
});