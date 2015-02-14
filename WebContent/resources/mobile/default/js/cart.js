$(function($){

	function getCartAmount() {
		$.ajax({
			url: base+"/cart/getCartAmount.do",
			type: "POST",
			dataType: "json",
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					$("#J_total_quantity").html(json.result.totalQuantity);
					$("#J_total_price").html(json.result.totalPrice.toFixed(2));
					if (json.result.discount<1){
						$("#J_discount").html(json.result.discount+'(会员折扣)');
					}else{
						$("#J_discount").html('');
					}
				}else{
					alert(json.message);
				}
			}
		});
	}
	getCartAmount(); // 计算总价

	function updateCart(id,quantity) {
		$.ajax({
			url: base+"/cart/update.do",
			type: "POST",
			dataType: "json",
			data: {id:id,quantity:quantity},
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					$("#J_number"+id).val(quantity);
					getCartAmount();
				}else{
					alert(json.message);
				}
			}
		});
	}

	$('.J_add').click(function() {
		var $this = $(this);
		var id=$this.data("id");
		var stock=parseInt($("#J_stock"+id).val());
		var number=parseInt($("#J_number"+id).val());
		var quantity = number+1;
		if (quantity>stock){
			$('#J_tips').html('当前库存剩余：'+stock).show().delay(2000).fadeOut(1000);
			quantity=stock;
		}
		if (quantity<1){
			quantity=1;
		}
		if (quantity!=number){
			updateCart(id,quantity);
		}
	});

	$('.J_redu').click(function() {
		var $this = $(this);
		var id=$this.data("id");
		var stock=parseInt($("#J_stock"+id).val());
		var number=parseInt($("#J_number"+id).val());
		var quantity = number-1;
		if (quantity>stock){
			$('#J_tips').html('当前库存剩余：'+stock).show().delay(2000).fadeOut(1000);
			quantity=stock;
		}
		if (quantity<1){
			quantity=1;
		}
		if (quantity!=number){
			updateCart(id,quantity);
		}
	});

	$('.J_number').change(function() {
		var $this = $(this);
		var id=$this.data("id");
		var stock=parseInt($("#J_stock"+id).val());
		var quantity=parseInt($this.val());
		if (quantity>stock){
			$('#J_tips').html('当前库存剩余：'+stock).show().delay(2000).fadeOut(1000);
			quantity=stock;
		}
		if (quantity<1){
			quantity=1;
		}
		updateCart(id,quantity);
	});

	$('.J_del').click(function() {
		if (!confirm('您确定要删除此商品吗？')){
			return;
		}
		var $this = $(this);
		var id=$this.data("id");
		$.ajax({
			url: base+"/cart/delete.do",
			type: "POST",
			dataType: "json",
			data: {id:id},
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					$this.closest('li').remove();
					// 总数量
				}else{
					alert(json.message);
				}
			}
		});
	});

	$('#J_clear').click(function() {
		if (!confirm('您确定要清空购物车吗？')){
			return;
		}
		$.ajax({
			url: base+"/cart/clear.do",
			type: "POST",
			dataType: "json",
			cache: false,
			success: function (json) {
				if (json.status=="success"){
					$('.product-li').remove();
					$("#J_total_quantity").html(0);
					$("#J_total_price").html('0.00');
					$("#J_discount").html('');
				}else{
					alert(json.message);
				}
			}
		});
	});
});
