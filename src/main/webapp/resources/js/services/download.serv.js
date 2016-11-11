'use strict';

/* Services */
app.factory('downloadFactory', function($q, $filter, $timeout, $http) {
    var downloadFactory = {};

	downloadFactory.getPackages = function(category, start, offset, params) {
        var deferred = $q.defer();
        var direction = params.sort.reverse == true ? "desc" : "asc";
        var result = {};
        var totalPage;
        $http.get('/dev/standard/'+category+'/packages', {
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
    
    return downloadFactory;
});