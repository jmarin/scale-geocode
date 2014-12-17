 $(function() {
   function log(message) {
     console.log(message);
   }

   var url = "/address/point/suggest";

   $("#address").autocomplete({
     source: function(request, response) {
       $.getJSON(url, {
         queryString: request.term
       }, function(data) {
         var array = data.error ? [] : $.map(data, function(item){
           return {
             label: item.properties.ADDRESS,
             feature: item
           };
         });
         response(array);
       });
     },
     minLength: 3,
     delay: 300,
     select: function(event, ui) {
       log(ui.item)
     }
   });
 });

