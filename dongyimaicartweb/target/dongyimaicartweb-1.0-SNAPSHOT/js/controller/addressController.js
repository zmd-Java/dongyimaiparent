 //控制层 
app.controller('addressController' ,function($scope,$controller   ,addressService){	
	
	$controller('baseController',{$scope:$scope});//继承

	//查询购物车列表
	$scope.findCartList = function () {
		addressService.findCartList().success(
			function (response) {
				$scope.cartList = response;
				$scope.totalValue = addressService.sum($scope.cartList);//求合计数
			}
		)
	};


	$scope.selectAddress = function(addr){
		$scope.address = addr;
	}

	$scope.isSelectedAddress = function(addr){

		// alert($scope.address.contact + " ---- "+ addr.contact);
		//
		// alert($scope.address == addr);

		if($scope.address == addr){
			return true;
		}else{
			return false;
		}

	}

    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		addressService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		addressService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		addressService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=addressService.update( $scope.entity ); //修改  
		}else{
			serviceObject=addressService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		addressService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		addressService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//获取地址列表
	$scope.findAddressList=function(){
		addressService.findAddressList().success(
			function(response){
				$scope.addressList=response;
				//设置默认地址
				for(var i=0;i< $scope.addressList.length;i++){
					if($scope.addressList[i].isDefault=='1'){
						$scope.address=$scope.addressList[i];
						break;
					}
				}
			}
		);
	}


	//保存订单
	$scope.submitOrder=function(){
		$scope.order.receiverAreaName=$scope.address.address;//地址
		$scope.order.receiverMobile=$scope.address.mobile;//手机
		$scope.order.receiver=$scope.address.contact;//联系人
		addressService.submitOrder( $scope.order ).success(
			function(response){
				if(response.success){
					//页面跳转
					if($scope.order.paymentType=='1'){//如果是扫码支付，跳转到支付页面
						location.href="pay.html";
					}else{//如果货到付款，跳转到提示页面
						location.href="paysuccess.html";
					}
				}else{
					alert(response.message);	//也可以跳转到提示页面
				}
			}
		);
	}

});	