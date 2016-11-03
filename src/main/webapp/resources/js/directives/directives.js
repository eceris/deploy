'use strict';

app.directive('modal', function () {
	return {
		template: '<div class="modal fade">' + 
		'<div class="modal-dialog">' + 
		'<div class="modal-content">' + 
		'<div class="modal-header">' + 
		'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' + 
		'<h4 class="modal-title">{{ title }}</h4>' + 
		'</div>' + 
		'<div class="modal-body" ng-transclude></div>' + 
		'</div>' + 
		'</div>' + 
		'</div>',
		restrict: 'E',
		transclude: true,
		replace:true,
		scope:true,
		link: function postLink(scope, element, attrs) {
			scope.title = attrs.title;

			scope.$watch(attrs.visible, function(value){
				if(value == true)
					$(element).modal('show');
				else
					$(element).modal('hide');
			});

			$(element).on('shown.bs.modal', function(){
				scope.$apply(function(){
					scope.$parent[attrs.visible] = true;
				});
			});

			$(element).on('hidden.bs.modal', function(){
				scope.$apply(function(){
					scope.$parent[attrs.visible] = false;
				});
			});
		}
	};
});

app.directive('ng-upload', function () {
   return {
       restrict: 'A',
       link: function (scope, element, attrs) {

           // Options (just 1 for now) 
           // Each option should be prefixed with 'upload-Options-' or 'uploadOptions'
           // {
           //    // specify whether to enable the submit button when uploading forms
           //    enableControls: bool 
           // }
           var options = {};
           options.enableControls = attrs['uploadOptionsEnableControls'];
           
           // get scope function to execute on successful form upload
           if (attrs['ng-upload']) {
               element.attr("target", "upload_iframe");
               element.attr("method", "post");

               // Append a timestamp field to the url to prevent browser caching results
               element.attr("action", element.attr("action") + "?_t=" + new Date().getTime());

               element.attr("enctype", "multipart/form-data");
               element.attr("encoding", "multipart/form-data");

               // Retrieve the callback function
               var fn = attrs['ng-upload'].split('(')[0];
               var callbackFn = scope.$eval(fn);
               if (callbackFn == null || callbackFn == undefined || !angular.isFunction(callbackFn))
               {
                   var message = "The expression on the ng-upload directive does not point to a valid function.";
                   // console.error(message);
                   throw message + "\n";
               }                      

               // Helper function to create new iframe for each form submission
               var addNewDisposableIframe = function (submitControl) {
                   // create a new iframe
                   var iframe = $("<iframe id='upload_iframe' name='upload_iframe' border='0' width='0' height='0' style='width: 0px; height: 0px; border: none; display: none' />");

                   // attach function to load event of the iframe
                   iframe.bind('load', function () {

                           // get content - requires jQuery
                           var content = iframe.contents().find('body').text();

                           // execute the upload response function in the active scope
                           scope.$apply(function () { callbackFn(content, content !== "" /* upload completed */); });

                           // remove iframe
                           if (content != "") // Fixes a bug in Google Chrome that dispose the iframe before content is ready.
                               setTimeout(function () { iframe.remove(); }, 250);

                           //if (options.enableControls == null || !(options.enableControls.length >= 0))
                           submitControl.attr('disabled', null);
                           submitControl.attr('title', 'Click to start upload.');
                       });

                   // add the new iframe to application
                   element.parent().append(iframe);
               };

               // 1) get the upload submit control(s) on the form (submitters must be decorated with the 'ng-upload-submit' class)
               // 2) attach a handler to the controls' click event
               $('.upload-submit', element).click(
                   function () {

                       addNewDisposableIframe($(this) /* pass the submit control */);

                       scope.$apply(function () { callbackFn("Please wait...", false /* upload not completed */); });

                       //console.log(angular.toJson(options));

                       var enabled = true;
                       if (options.enableControls === null || options.enableControls === undefined || options.enableControls.length >= 0) {
                           // disable the submit control on click
                           $(this).attr('disabled', 'disabled');
                           enabled = false;
                       }

                       $(this).attr('title', (enabled ? '[ENABLED]: ' : '[DISABLED]: ') + 'Uploading, please wait...');

                       // submit the form
                       $(element).submit();
                   }
               ).attr('title', 'Click to start upload.');
           }
           else
               console.log("No callback function found on the ng-upload directive.");
       }
   };
});