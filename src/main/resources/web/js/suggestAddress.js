 $(function() {

   var map = L.map('map').setView([38.8951100, -77.0363700], 13);

   L.tileLayer('http://{s}.tiles.mapbox.com/v3/examples.map-i87786ca/{z}/{x}/{y}.png', {
       attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
       maxZoom: 18
   }).addTo(map);

   var layer = L.geoJson();

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
       layer.clearLayers();
       var feature = ui.item.feature;
       //log(feature);
       var coordinates = feature.geometry.coordinates;
       layer.addData(feature);
       layer.addTo(map);
       map.setView([coordinates[1], coordinates[0]], 13);
     }
   });

   function removeLayer(layer) {
     map.removeLayer(layer);
   }

 });

