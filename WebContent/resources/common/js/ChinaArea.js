/**
 * 省市区联动
 * 初始化：ChinaArea.init({base:"${base}",areaCode:"350502"});
 * wuduanpiao at 2014-02-27
 */
var ChinaArea = {
	opts : {
		base : "", // 根路径
		code : "", // 初始值
		valueField : "code",
		textField : "name",
		provinceSelector : "#province",
		citySelector : "#city",
		districtSelector : "#district",
		areaCodeSelector : "#areaCode"
	},
	province : null,
	city : null,
	district : null,
	areaCode : null,

	init : function(options) {
		var $this = this;
		$this.opts = $.extend($this.opts, options);
		$this.province = $($this.opts.provinceSelector);
		$this.city = $($this.opts.citySelector);
		$this.district = $($this.opts.districtSelector);
		$this.areaCode = $($this.opts.areaCodeSelector);

		//$("#location").append('<select name="province" id="province"></select><select name="city" id="city"></select><select name="district" id="district"></select><input type="hidden" name="areaCode" id="areaCode" value="" />');
		$this.province.bind("change",function(){$this.loadCity();$this.selectAreaCode();});
		$this.city.bind("change",function(){$this.loadDistrict();$this.selectAreaCode();});
		$this.district.bind("change",function(){$this.selectAreaCode();});

		$this.loadProvince();
		$this.areaCode.val($this.opts.code);
	},

	loadProvince : function() {
		var $this = this;
		$this.city.empty();
		$this.district.empty();
		if ($this.province.length==0){alert("初始化省份失败");return;}
		if ($this.province.children().size()==0){
			$.getJSON(
				$this.opts.base+"/china/provinces.do",
				function(json) {
					//$this.province.empty();
					$this.province.append('<option value=""></option>');
					for (i=0; i<json.length; i++){
						$this.province.append('<option value="'+json[i][$this.opts.valueField]+'">'+json[i][$this.opts.textField]+'</option>');
					}
					$this.selectProvince($this.opts.code);
					//if(true==__G__IE7){$this.city.hide().show();} // ie7重叠
				}
			);
		}else{
			$this.selectProvince($this.opts.code);
		}
	},

	selectProvince : function(code) {
		var $this = this;
		var value = "";
		if (/\d{2,6}/.test(code)){
			value = code.substr(0,2)+"0000";
		}
		$this.province.val(value);
		$this.loadCity(true);
	},

	loadCity : function(autoSelect) {
		var $this = this;
		$this.city.empty();
		$this.district.empty();
		var pCode = $this.province.val();
		if(pCode && pCode.substr(0,2)>'00') {
			$.getJSON(
				$this.opts.base+"/china/cities/"+ pCode +".do",
				function(json) {
					$this.city.append('<option value=""></option>');
					for (i=0; i<json.length; i++){
						$this.city.append('<option value="'+json[i][$this.opts.valueField]+'">'+json[i][$this.opts.textField]+'</option>');
					}
					if (autoSelect){
						$this.selectCity($this.opts.code);
					}
					//if(true==__G__IE7){district.hide().show();} // ie7重叠
				}
			);
		}
	},

	selectCity : function(code) {
		var $this = this;
		var value = "";
		if (/\d{4,6}/.test(code)){
			value = code.substr(0,4)+"00";
		}
		$this.city.val(value);
		$this.loadDistrict(true);
	},

	loadDistrict : function(autoSelect) {
		var $this = this;
		$this.district.empty();
		var cCode = $this.city.val();
		if(cCode && cCode.substr(2,4)>'00') {
			$.getJSON(
				$this.opts.base+"/china/districts/"+ cCode +".do",
				function(json) {
					$this.district.append('<option value=""></option>');
					for (i=0; i<json.length; i++){
						$this.district.append('<option value="'+json[i][$this.opts.valueField]+'">'+json[i][$this.opts.textField]+'</option>');
					}
					if (autoSelect){
						$this.selectDistrict($this.opts.code);
					}
				}
			);
		}
	},

	selectDistrict : function(code) {
		var $this = this;
		var value = "";
		if (/\d{6}/.test(code)){
			value = code;
		}
		$this.district.val(value);
	},

	selectAreaCode : function() {
		var $this = this;
		var value = "";
		if ($this.district.val()){
			value = $this.district.val();
		} else if ($this.city.val()){
			value = $this.city.val();
		} else if ($this.province.val()){
			value = $this.province.val();
		}
		$this.areaCode.val(value);
	}
}