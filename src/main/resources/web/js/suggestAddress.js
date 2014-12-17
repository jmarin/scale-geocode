 $(function() {

	 function log(message) {
	   console.log(message);
	 }			 

   $("#address").autocomplete({
	   source: "/address/point/geocode" + $("#address").val(),
		 minLength: 3,
		 delay: 300,
	   select: function(event, ui) {
		   log(ui.item.value)
		 }
	 });
 });

