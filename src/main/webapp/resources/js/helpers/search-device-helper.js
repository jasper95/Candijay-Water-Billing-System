/**
 * Created by Bert on 2/22/2017.
 */
$(document).ready(function(){
   $('#search-dv').on('click', function(){
       $('#search-device-info-modal').modal('show');
   });
   $('#submit-search-dv').on('click', function(){
       var mc = $('#search-mc').val().trim();
       if(mc){
           $('#dv-mc-filter').find('input:first').val(mc);
           $('#triggerSearch').trigger('click');
       }
   });
});