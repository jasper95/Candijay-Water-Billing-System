/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var monthFilters = [{"value":1, "label":"Jan"},{"value":2, "label":"Feb"},{"value":3, "label":"Mar"},
    {"value":4, "label":"Apr"},{"value":5, "label":"May"},{"value":6, "label":"June"},
    {"value":7, "label":"Jul"},{"value":8, "label":"Aug"},{"value":9, "label":"Sept"},
    {"value":10, "label":"Oct"},{"value":11, "label":"Nov"},{"value":12, "label":"Dec"}];
var yearFilters = [];
for (i = new Date().getFullYear(); i >= 2007; i--)
{    yearFilters.push(i);
}