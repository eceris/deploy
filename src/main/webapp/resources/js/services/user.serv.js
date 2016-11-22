'use strict';

app.factory('userFactory', function($q, $filter, $timeout, $http) {
    var userFactory = {};
    
    userFactory.getUsers = function(start, offset, params) {
        var deferred = $q.defer();
        var direction = params.sort.reverse == true ? "desc" : "asc";
        var result = {};
        var totalPage;
        $http.get('/adm/users', {
                params: {
                    page: start / offset,
                    offset: offset,
                    property: params.sort.predicate,
                    direction: direction
                }
            }).success(function(data, status, headers, config) {
                if (data) {
                    result = data.content;
                    totalPage = parseInt(data.pageInfo.total / data.pageInfo.offset) + 1;
                }
                deferred.resolve({
                    data: result,
                    numberOfPages: totalPage
                });
            })
            .error(function(data, status, headers, config) {
                console.log(status);
            });

        return deferred.promise;
    };
    
    userFactory.create = function(user) {
        return $http({
            method: 'POST',
            url: '/adm/user',
            data: user
        });
    };

    userFactory.get = function(id) {
        return $http({
            method: 'GET',
            url: '/adm/user/' + id
        });
    };

    userFactory.update = function(user) {
        return $http({
            method: 'PUT',
            url: '/adm/user/',
            data: user
        });
    };
    
    userFactory.changepassword = function(password) {
    	var obj = {key : "password", value : password.first};
        return $http({
            method: 'PUT',
            url: '/user/password',
            data : JSON.stringify(obj)
        });
    };

    userFactory.delete = function(user) {
        return $http({
            method: 'DELETE',
            url: '/adm/user/' + user.id
        });
    };

    userFactory.generateNamespaces = function(obj) {
        var param = [];
        $.each(obj, function(i, item) {
            if (item.value || 0) {
                param.push(item.path);
            }
        });
        var str = "";
        $.each(param, function(i, item) {
            str = str.concat(item);
            if (param.length - 1 == i) {
                return;
            }
            str = str.concat(';');
        });
        return str;
    };

    userFactory.isCreate = function(id) {
        return id == 'create';
    };

    userFactory.getNamespaces = function() {
        return $http({
            method: 'GET',
            url: '/namespaces/'
        });
    };


    return userFactory;
});