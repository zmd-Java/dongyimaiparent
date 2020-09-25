app.controller('itemController',function($scope,$http){

	$scope.addToCart = function(){
		// alert("加入购物车"+$scope.defaultSku.id);
		$http.get('http://localhost:9107/cart/addGoodsToCartList.do?itemId='
			+ $scope.defaultSku.id +'&num='+$scope.num,{'withCredentials':true}).success(
			function(response){
				if(response.success){
					location.href='http://localhost:9107/cart.html';//跳转到购物车页面
				}else{
					alert(response.message);
				}
			}
		);


	}

	$scope.loadSku = function(){
		$scope.defaultSku = itemList[0];
		$scope.specification = JSON.parse(JSON.stringify($scope.defaultSku.spec));
	}
	
	
	searchSku = function(){
		
		for(var i=0;i<itemList.length;i++){
			if(matchObject(itemList[i].spec,$scope.specification)){
			
				$scope.defaultSku = itemList[i];
				
				return ;
			}
		}
		
		$scope.defaultSku={id:0,title:'--------',price:0};//如果没有匹配的		
	}
	
	matchObject = function(map1,map2){
		
		for(var k in map1){
			
			if(map1[k] != map2[k]){
				return false;
			}
		}
		
		for(var k in map2){
		
			if(map2[k] != map1[k]){
				return false;
			}
		}
		
		return true;
	}
	
	
	$scope.addNum = function(x){
		$scope.num = $scope.num+x;
		
		if($scope.num <1){
			$scope.num = 1;
		}
		
		if($scope.num > 200){
			$scope.num = 200;
		}
		
	}

	//规格选择对象
	$scope.specification = {};
	
	//用户选择规格 将用户选择的规格 装入 上面定义的对象中
	$scope.addSpec = function(name,value){
		$scope.specification[name] = value;
		searchSku();
	}
	
	//判断样式 如果选择了 则返回true 使用最被选择的样式
	$scope.isSelected = function(name,value){

		if($scope.specification[name] == value){
			return true;
		}else{
			return false;
		}
	}
	
})