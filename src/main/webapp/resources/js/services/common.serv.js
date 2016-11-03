'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
//angular.module('myApp.services', []).
//  value('version', '0.1');



app.factory('fileFactory', function($q, $filter, $timeout, $http) {
    var fileFactory = {};

    fileFactory.fileSync = function() {
        return $http.get('/file/sync');
    };

    fileFactory.fileSyncDelete = function(filename) {
        return $http.delete('/file/sync/' + filename);
    };

    fileFactory.getFileboxList = function() {
        return $http.get('/file/filebox');
    };

    fileFactory.getPackageInfo = function(searchKey) {
    	return $http({
            method: 'GET',
            url: '/file/search',
            params: { searchKey : searchKey}
        });
//        return $http.get('/file/search/' + searchKey);
    };

    fileFactory.deleteFile = function(seq) {
        return $http.delete('/file/' + seq);
    };

    fileFactory.getHistoryList = function() {
        return $http.get('/history/download');
    };

    fileFactory.getHistoryUpgradeList = function () {
	    return $http.get('/history/upgrade');
    };

    fileFactory.getAttachPagination = function(start, offset, params) {
        var deferred = $q.defer();
        var direction = params.sort.reverse == undefined ? "desc" : params.sort.reverse == true ? "desc" : "asc";
        var result = {};
        var totalPage;
        $http.get('/file/list', {
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

    return fileFactory;
});

app.factory('coreFileFactory', function($q, $filter, $timeout, $http) {
    var fileFactory = {};

    fileFactory.getAttachPagination = function(start, offset, params) {
        var deferred = $q.defer();
        var direction = params.sort.reverse == undefined ? "desc" : params.sort.reverse == true ? "desc" : "asc";
        var result = {};
        var totalPage;
        var brand = "";
        var type = "";
        if(params.search.predicateObject){
            brand = params.search.predicateObject.brand == undefined ? "" : params.search.predicateObject.brand;
            type = params.search.predicateObject.type == undefined ? "" : params.search.predicateObject.type;
        }

        if(brand == "" || type == ""){
            deferred.resolve({
                data: [],
                numberOfPages: 0
            });
            return deferred.promise;
        }

        $http.get('/file/package', {
                params: {
                    page: start / offset,
                    offset: offset,
                    property: params.sort.predicate,
                    direction: direction,
                    type: type,
                    brand: brand
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

    return fileFactory;
});



app.factory('patchFactory', function($q, $filter, $timeout, $http)  {
    var patchFactory = {};
    var domain;
    var hostId;
    var type;
    var id;
    var pw;
    var path;
    patchFactory.getPackage = function(category, site, start, number, params) {
        var deferred = $q.defer();
        $http.get('/file/search?searchKey='+site + '&category='+category)
        	.success(function(data, status, headers, config) {
        		//client 정렬
        		var filtered = params.search.predicateObject ? $filter('filter')(data, params.search.predicateObject) : data;
        		if (params.sort.predicate) {
        			filtered = $filter('orderBy')(filtered, params.sort.predicate, params.sort.reverse);
        		}
        		var result = filtered.slice(start, start + number);
                deferred.resolve({
    				data: result,
    				numberOfPages: Math.ceil(filtered.length / number)
    			});
            })
            .error(function(data, status, headers, config) {
                console.log(status);
            });

        return deferred.promise;
    };
    
    patchFactory.upload = function(domain, hostId, category, id, pw, path) {
    	this.domain = domain;
    	this.hostId = hostId;
    	this.category = category;
    	this.id = id;
    	this.pw = pw;
    	this.path = path;
    	
        return $http({
            method: 'POST',
            url: '/customer/patch/upload',
            params: { domain : domain, hostId : hostId, category : category, id : id, pw : pw, path : path }
        });
    };
    
    patchFactory.execute = function() {
        return $http({
            method: 'POST',
            url: '/customer/patch/execute',
            params: { domain : this.domain, hostId : this.hostId, category : this.category, path : this.path }
        });
    };
    
    return patchFactory;
});