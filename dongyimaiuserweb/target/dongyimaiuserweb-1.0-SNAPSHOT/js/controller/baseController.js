app.controller('baseController',function ($scope) {

    //定义一个数组 [1,2,3,4]
    $scope.selectIds = [];

    //当点击复选框时调用函数
    $scope.updateSelection = function($event,id){
        //判断该元素是否被选中 如果被选中
        if($event.target.checked){
            $scope.selectIds.push(id);
        }else{
            //如果没有被选中 获取该元素在数组中的索引(下标)
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index,1);
        }
        //alert("ids:"+$scope.selectedIds);
    }

    //重新加载列表 数据
    $scope.reloadList=function(){
        //切换页码
        //$scope.findPages( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        $scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    };

});