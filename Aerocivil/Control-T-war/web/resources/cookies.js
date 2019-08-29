/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var arrSplit = document.cookie.split(";");

for(var i = 0; i < arrSplit.length; i++)
{
    var cookie = arrSplit[i].trim();
    var cookieName = cookie.split("=")[0];

    // If the prefix of the cookie's name matches the one specified, remove it
    if(cookieName.indexOf("panelMenu") === 0) {

        // Remove the cookie
        document.cookie = cookieName + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}
