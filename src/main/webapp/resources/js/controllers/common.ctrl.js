'use strict';

/* Controllers */
app.controller('fileboxListCtrl', function($scope, $http, $modal, fileFactory) {
	$scope.getFileboxList = function() {
		fileFactory.getFileboxList().success(function(data, status, headers, config) {
			if (data) {
				$scope.fileData = data;
			}
		})
		.error(function(data, status, headers, config) {
			console.log(status);
		});
	};

	$scope.sync = function() {
		fileFactory.fileSync()
		.success(function(data, status, headers, config) {
			$modal.open({
	        	templateUrl: '/resources/partials/dialogView/syncSuccess.html',
	            controller: 'ModalContactDetailController',
	            size: 'sm',
	            resolve: {
	                contact: function () {
	                    return ;
	                }
	            }
	        });
			$scope.getFileboxList();
			console.log(status);
		})
		.error(function(data, status, headers, config) {
			var file = data + ",파일을 동기화 하는데 실패했습니다.";
			file = file.split(",")
			$modal.open({
	        	templateUrl: '/resources/partials/dialogView/syncAlert.html',
	            controller: 'ModalContactDetailController',
	            size: 'sm',
	            resolve: {
	                contact: function () {
	                	//$scope.filename = file;
	                    return file;
	                }
	            }
	        });
			//alert(file + "파일을 동기화 하는데 실패했습니다.");
			$scope.getFileboxList();
			console.log(status);
		});
	};

	$scope.syncDelete = function(filename) {
		$modal.open({
			templateUrl: '/resources/partials/dialogView/deleteWarning.html',
			controller: 'ModalCtrl',
			size: 'sm',
			resolve: {
				contact: function() {
					return filename;
				}
			}
		});
	};
	$scope.getFileboxList();
});

app.controller('attachPaginationCtrl', function($scope, $filter, fileFactory) {
	
	var ctrl = this;
	
	ctrl.isClick = false;
	ctrl.displayed = [];
	ctrl.callServer = function callServer(tableState) {
		ctrl.isLoading = true;
		var pagination = tableState.pagination;
		var start = pagination.start || 0; // This is NOT the page number, but the index of item in the list that you want to use to display the table.
		var offset = pagination.number || 10; // Number of entries showed per page.
		
		fileFactory.getAttachPagination(start, offset, tableState).then(function(result) {
			ctrl.displayed = result.data;
			tableState.pagination.numberOfPages = result.numberOfPages; //set the number of pages so the pagination can update
			ctrl.isLoading = false;
		});
	};
	
});

app.controller('coreAttachPaginationCtrl', function($scope, $filter, coreFileFactory) {

	var ctrl = this;

	ctrl.isClick = false;
	ctrl.displayed = [];
	ctrl.callServer = function callServer(tableState) {
		ctrl.isLoading = true;
		var pagination = tableState.pagination;
		var start = pagination.start || 0; // This is NOT the page number, but the index of item in the list that you want to use to display the table.
		var offset = pagination.number || 10; // Number of entries showed per page.

		coreFileFactory.getAttachPagination(start, offset, tableState).then(function(result) {
			ctrl.displayed = result.data;
			tableState.pagination.numberOfPages = result.numberOfPages; //set the number of pages so the pagination can update
			ctrl.isLoading = false;
		});
	};

	$scope.packageDownload = function(brand, name) {
		var params = {
			"brand" : brand,
			"name" : name
		}
		window.location = '/file/package/download?' + $.param(params) ;
	};
}).directive('customSearch',function(){
	return {
		restrict:'E',
		require:'^stTable',
		templateUrl:'/resources/partials/downloadView/customSearch.html',
		scope:true,
		link:function(scope, element, attr, ctrl){
			var tableState=ctrl.tableState();
			scope.$watch('brandFilterValue',function(value){
				var brand = $("#customFilter #brand").val();
				var type = $("#customFilter #type").val();

				if(brand == "" || type == ""){
					return;
				}

				tableState.search.predicateObject ={};
				ctrl.search(brand,'brand');
				ctrl.search(type,'type');
			});
			scope.$watch('typeFilterValue',function(value){
				var brand = $("#customFilter #brand").val();
				var type = $("#customFilter #type").val();

				if(brand == "" || type == ""){
					return;
				}

				tableState.search.predicateObject ={};
				ctrl.search(brand,'brand');
				ctrl.search(type,'type');
			});
		}
	};
});

app.controller('fileListCtrl', function($scope, $http, $modal, fileFactory) {
	$scope.getFileList = function() {
		fileFactory.getFileList().success(function(data, status, headers, config) {
			if (data) {
				return data;
			}
		})
		.error(function(data, status, headers, config) {
			console.log(status);
		});
	};

	$scope.packageDownload = function(row) {
		window.location = '/file/download/' + row;
	};

	$scope.packageDelete = function(packageInfo) {

		var modalInstance = $modal.open({
			templateUrl: '/resources/partials/dialogView/deleteWarning.html',
			controller: 'PackageInfoModalCtrl',
			size: 'sm',
			resolve: {
				packageInfo: function() {
					return packageInfo;
				}
			}
		});
		modalInstance.result.then(function(packageInfo) {
			var index = $scope.pc.displayed.indexOf(packageInfo);
			if (index !== -1) {
				$scope.pc.displayed.splice(index, 1);
			}
		});
	};
	
	$scope.search = function() {
		$scope.pc.isClick = true;
		fileFactory.getPackageInfo($scope.searchText).success(function(data, status, headers, config) {
			$scope.pc.displayed = data;
		})
		.error(function(data, status, headers, config) {
			console.log(status);
		});
	};
});






app.controller('multiFileUploadCtrl', function($scope, FileUploader, $route, $modal) {
	var uploader = $scope.uploader = new FileUploader({
		url: '/file/upload'
	});
	uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/ , filter, options) {
		console.info('onWhenAddingFileFailed', item, filter, options);
	};
	uploader.onAfterAddingFile = function(fileItem) {
		console.info('onAfterAddingFile', fileItem);
	};
	uploader.onAfterAddingAll = function(addedFileItems) {
		console.info('onAfterAddingAll', addedFileItems);
	};
	uploader.onBeforeUploadItem = function(item) {
		console.info('onBeforeUploadItem', item);
	};
	uploader.onProgressItem = function(fileItem, progress) {
		console.info('onProgressItem', fileItem, progress);
	};
	uploader.onProgressAll = function(progress) {
		console.info('onProgressAll', progress);
	};
	uploader.onSuccessItem = function(fileItem, response, status, headers) {
		console.info('onSuccessItem', fileItem, response, status, headers);
	};
	uploader.onErrorItem = function(fileItem, response, status, headers) {
		$modal.open({
        	templateUrl: '/resources/partials/dialogView/filenameWarning.html',
            controller: 'ModalContactDetailController',
            size: 'sm',
            resolve: {
                contact: function () {
                    return $scope.contact;
                }
            }
        });
		//alert("파일 이름 형식을 확인해 주세요");
		console.info('onErrorItem', fileItem, response, status, headers);
	};
	uploader.onCancelItem = function(fileItem, response, status, headers) {
		console.info('onCancelItem', fileItem, response, status, headers);
	};
	uploader.onCompleteItem = function(fileItem, response, status, headers) {
		console.info('onCompleteItem', fileItem, response, status, headers);
	};
	uploader.onCompleteAll = function() {
		console.info('onCompleteAll');
		$modal.open({
        	templateUrl: '/resources/partials/dialogView/uploadAlert.html',
            controller: 'ModalContactDetailController',
            size: 'sm',
            resolve: {
                contact: function () {
                    return $scope.contact;
                }
            }
        });
		
		//alert("파일 업로드가 완료되었습니다");
		var updateSyncList = angular.element('[ng-controller="fileboxListCtrl"]').scope();
		updateSyncList.getFileboxList();
	};
});

app.controller('ModalCtrl', function($scope, $modalInstance, fileFactory, contact, $modal) {
//	$scope.user = contact;
//	$scope.ok = function() {
//		$modalInstance.close($scope.user);
//	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

	$scope.del = function() {
		fileFactory.fileSyncDelete(contact).success(function(data, status, headers, config) {
			var updateSyncList = angular.element('[ng-controller="fileboxListCtrl"]').scope();
			updateSyncList.getFileboxList();
		})
		.error(function(data, status, headers, config) {
			$modal.open({
	        	templateUrl: '/resources/partials/dialogView/deleteFail.html',
	            controller: 'ModalContactDetailController',
	            size: 'sm',
	            resolve: {
	                contact: function () {
	                    return ;
	                }
	            }
	        });
			console.log(status);
		});
		$modalInstance.close();
	};
});

app.controller('PackageInfoModalCtrl', function($scope, $modalInstance, fileFactory, packageInfo, $modal) {
	
	$scope.packageInfo = packageInfo;
	$scope.del = function() {
		fileFactory.deleteFile($scope.packageInfo.id).success(function(data, status, headers, config) {
			console.log(data);
		})
		.error(function(data, status, headers, config) {
			$modal.open({
	        	templateUrl: '/resources/partials/dialogView/deleteFail.html',
	            controller: 'ModalContactDetailController',
	            size: 'sm',
	            resolve: {
	                contact: function () {
	                    return ;
	                }
	            }
	        });
			console.log(status);
		});
		$modalInstance.close($scope.packageInfo);
	};
	
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
});

app.controller('ModalContactDetailController',function($scope, $modalInstance, contact) {
	$scope.customer = contact;
    $scope.files = contact;
    $scope.ok = function () {
        $modalInstance.close();
    };
    
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
    
    $scope.success = function () {
    	window.location = '/#customer';
        $modalInstance.close();
    };
});





app.controller('ModalController',function($scope, $modalInstance, data) {
    $scope.data = data;
    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.success = function () {
        $modalInstance.close();
    };
});

app.controller('PatchCtrl', function($scope, $http, $modal, patchFactory, customerFactory, $log) {
	$scope.customer = customerFactory.getCustomer();
    var ctrl = this;
    
    ctrl.standardDisplayed = [];
    ctrl.standardCallServer = function standardCallServer(tableState) {
        ctrl.isLoading = true;
        var pagination = tableState.pagination;
        var start = pagination.start || 0; // This is NOT the page number, but the index of item in the list that you want to use to display the table.
        var number = pagination.number || 10; // Number of entries showed per page.
        patchFactory.getPackage('daouoffice', $scope.customer.site, start, number, tableState).then(function(result) {
            ctrl.standardDisplayed = result.data;
            tableState.pagination.numberOfPages = result.numberOfPages; //set the number of pages so the pagination can update
            ctrl.isLoading = false;
        });
    };
    
    ctrl.customDisplayed = [];
    ctrl.customCallServer = function customCallServer(tableState) {
        ctrl.isLoading = true;
        var pagination = tableState.pagination;
        var start = pagination.start || 0; // This is NOT the page number, but the index of item in the list that you want to use to display the table.
        var number = pagination.number || 10; // Number of entries showed per page.
        patchFactory.getPackage('custom', $scope.customer.site, start, number, tableState).then(function(result) {
            ctrl.customDisplayed = result.data;
            tableState.pagination.numberOfPages = result.numberOfPages; //set the number of pages so the pagination can update
            ctrl.isLoading = false;
        });
    };
    
    $scope.upload = function(domain) {
    	var id = angular.element($("#siteId")).val();
    	var pw = angular.element($("#sitePw")).val();
    	var hostId = angular.element($("#hostId")).attr('data');
		var target = angular.element($(".st-selected"));
    	var category = target.attr('data-type');
    	var filePath = target.attr('data-attach-path');
	
    	if(!(id || 0)) {
    		alert('아이디를 입력하세요.')
    		return;
    	}
    	if(!(pw || 0)) {
    		alert('비밀번호를 입력하세요.')
    		return;
    	}
    	if(!(filePath || 0)) {
    		alert('파일을 선택하세요.')
    		return;
    	}
    	
    	if(!(hostId || 0)) {
    		alert('호스트아이디를 추가하세요.')
    		return;
    	}
    	patchFactory.upload(domain, hostId, category, id, pw, filePath)
    	.success(function(data, status, headers, config) {
    		alert('파일 업로드 성공');
    		console.log(data);
    		
    		var modalInstance = $modal.open({
        		templateUrl: '/resources/partials/dialogView/patchConfirm.html',
                size: 'sm',
                controller: 'PatchModalCtrl',
                resolve: {
                    data: function () {
                        return;
                    }
                }
            });
        	
            modalInstance.result.then(function() {
            	$scope.cancel();
            });
			
		})
		.error(function(data, status, headers, config) {
			alert('파일 업로드 실패');
			console.log(status);
			return;
		});
    	
    };
    
    $scope.cancel = function($modal) {
        this.$close();
    };
    
    $scope.move = function($modal) {
    	angular.element($(".st-selected")).removeClass('st-selected');
    };
    
    
});


app.controller('PatchModalCtrl',function($scope, $modalInstance, patchFactory, data) {
	$scope.domain = data;
    $scope.patch = function () {
    	patchFactory.execute()
    	.success(function(data, status, headers, config) {
    		alert('패치가 시작되었습니다.');
			console.log(data);
		})
		.error(function(data, status, headers, config) {
			alert('실패');
			console.log(status);
		});
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.success = function () {
        $modalInstance.close();
    };
});
