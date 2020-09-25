//服务层
app.service('addressService',function($http){

	//获取地址列表
	this.findAddressList=function(){
		return $http.get('address/findListByLoginUser.do');
	}

	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../address/findAll.do');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../address/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../address/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../address/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../address/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../address/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../address/search.do?page='+page+"&rows="+rows, searchEntity);
	}

	//购物车列表
	this.findCartList=function () {
		return $http.get('cart/findCartList.do');
	}

	this.sum=function (cartList) {
		var totalValue = {totalNum:0,totalMoney:0.00};//合计实体
		for (var i = 0;i<cartList.length;i++){
			var cart=cartList[i];
			for(var j=0;j<cart.orderItemList.length;j++){
				var orderItem=cart.orderItemList[j];//购物车明细
				totalValue.totalNum += orderItem.num;
				totalValue.totalMoney += orderItem.totalFee;
			}
		}
		return totalValue;
	}

	//保存订单
	this.submitOrder=function(order){
		return $http.post('order/add.do',order);
	}
});