'use strict';

/* Controllers */
app.controller('userCtrl', function($scope, $http, $modal, $location, userFactory, $log) {
    /* page */
    var ctrl = this;
    ctrl.displayed = [];
    ctrl.callServer = function callServer(tableState) {
        ctrl.isLoading = true;
        var pagination = tableState.pagination;
        var start = pagination.start || 0;
        var offset = pagination.number || 10;
        userFactory.getUsers(start, offset, tableState).then(function(result) {
            ctrl.displayed = result.data;
            tableState.pagination.numberOfPages = result.numberOfPages;
            ctrl.isLoading = false;
        });
    };

    $scope.goToCreate = function() {
        $location.url('/user/create');
    };
    
    $scope.goToUpdate = function(user) {
        $location.url('/user/'+user.id);
    };
});

app.controller('userDetailCtrl', function($scope, $routeParams, $http, userFactory, $log, $location) {
    $scope.id = $routeParams.id;

    $scope.goToList = function() {
        $location.url('/users');
    };
    
    $scope.goToHome = function() {
        $location.url('/projects');
    };

    $scope.init = function(id) {
        // 네임스페이스 바인딩
        userFactory.getNamespaces()
            .success(function(data, status, headers, config) {
                if (data) {
                    $scope.namespaces = data;

                    if (userFactory.isCreate(id)) {
                        $scope.role = 'ROLE_DEVELOPER';
                    } else {
                        userFactory.get(id)
                            .success(function(data, status, headers, config) {
                                if (data) {
                                    console.log(data);
                                    $scope.role = data.role;
                                    $scope.user = {
                                        id: data.id,
                                        name: data.name,
                                        loginId: data.loginId,
                                        email: data.email
                                    };

                                    if (data.gitGroup) { // 해당 사용자가 gitGroup을
															// 갖고 있으면
                                        $.each($scope.namespaces, function(i, item) {
                                            // gitGroup이 namespace를 갖고 있으면
                                            if (data.gitGroup.lastIndexOf(item.path) > -1) {
                                                item.value = true;
                                            }
                                        });

                                    }
                                }
                            })
                            .error(function(data, status, headers, config) {
                                console.log(data);
                                alert('데이터로딩 에러 : ' + data);
                            });
                    }
                }
            })
            .error(function(data, status, headers, config) {
                console.log(data);
                alert('데이터로딩 에러 : ' + data);
            });
    };

    $scope.create = function(user, role, namespaces) {
        user.role = role;
        user.gitGroup = userFactory.generateNamespaces(namespaces);
        userFactory.create(user)
            .success(function(data, status, headers, config) {
                if (data) {
                    console.log(data);
                    alert('계정 생성 완료');
                    $location.url('/users');
                }
            })
            .error(function(data, status, headers, config) {
                console.log(data);
                alert('계정 생성 실패 : ' + data);
                $location.url('/users');
            });
    };

    $scope.update = function(user, role, namespaces) {
    	user.role = role;
    	user.gitGroup = userFactory.generateNamespaces(namespaces);
        userFactory.update(user)
            .success(function(data, status, headers, config) {
                if (status == 200) {
                    console.log(data);
                    alert('수정 완료');
                    $location.url('/users');
                }
            })
            .error(function(data, status, headers, config) {
                console.log(data);
                alert('수정 실패 : ' + data);
                $location.url('/users');
            });
    };

    $scope.changepassword = function(password) {
    	if (password.first != password.second) {
    		alert('비빌번호가 다릅니다.');
    	}
        userFactory.changepassword(password)
            .success(function(data, status, headers, config) {
                if (status == 200) {
                    console.log(data);
                    alert('변경 완료');
                    $location.url('/projects');
                }
            })
            .error(function(data, status, headers, config) {
                console.log(data);
                alert('변경 실패 : ' + data);
                $location.url('/password');
            });
    };
    
    $scope.delete = function(user) {
        userFactory.delete(user)
            .success(function(data, status, headers, config) {
            	if (status == 200) {
                    console.log(data);
                    alert('계정 삭제 완료');
                    $location.url('/users');
                }
            })
            .error(function(data, status, headers, config) {
                console.log(data);
                alert('계정 삭제 실패 : ' + data);
                $location.url('/users');
            });
    };

});