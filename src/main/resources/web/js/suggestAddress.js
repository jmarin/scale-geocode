 $(document).ready(function() {

 var addresses = new Bloodhound({
   datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
   queryTokenizer: Bloodhound.tokenizers.whitespace,
   //prefetch: '../data/films/post_1960.json',
   remote: '/geocode/point/suggest?queryString=%QUERY'
 });

 addresses.initialize();

 $('#remote .typeahead').typeahead(null, {
   name: 'geocode',
   displayKey: 'value',
   source: addresses.ttAdapter()
 });

 });

