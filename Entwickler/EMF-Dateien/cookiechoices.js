!function(){var e="displayCookieConsent",t="cookieChoiceInfo",n="cookieChoiceDismiss",o="cookieChoiceDismissIcon";function i(){var e=window._wfCookieConsentSettings;void 0!==e&&void 0!==e.wf_linkhref&&(e.styles="position:fixed; width:100%; background-color:#EEEEEE; background-color:rgba(238, 238, 238, 0.9); margin:0; left:0; "+e.wf_position+":0; padding:4px; z-index:1000; text-align:center;",s(h(e.wf_cookietext),h(e.wf_dismisstext),h(e.wf_linktext),e.wf_linkhref,e.styles,!1))}function a(e,n,i,a,d){var p,s=d,u=document.createElement("div"),h=document.createElement("div");return h.style.cssText="padding-right: 50px;",u.id=t,u.style.cssText=s,h.appendChild(c(e)),i&&a&&h.appendChild(l(i,a)),h.appendChild(r(n)),u.appendChild(h),u.appendChild(((p=document.createElement("a")).id=o,p.href="#",p.style.cssText="width: 50px; height: 100%; background-size: 20px; display: inline-block; position: absolute; right: 0px; top: 0px; background-position: 34% 50%; background-color: #CCCCCC; background-color: rgba(204, 204, 204, 0.6); background-repeat: no-repeat; background-image: url(data:image/png;base64,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);",p)),u}function d(e,t){"textContent"in document.body?e.textContent=t:e.innerText=t}function c(e){var t=document.createElement("span");return d(t,e),t}function r(e){var t=document.createElement("a");return d(t,e),t.id=n,t.href="#",t.style.marginLeft="24px",t}function l(e,t){var n=document.createElement("a");return d(n,e),n.href=t,n.target="_blank",n.rel="noopener",n.style.marginLeft="8px",n}function p(){var t;return(t=new Date).setFullYear(t.getFullYear()+1),document.cookie=e+"=y;path=/; expires="+t.toGMTString(),u(),!1}function s(i,d,s,h,m,f){if(!document.cookie.match(new RegExp(e+"=([^;]+)"))){u();var g=f?function(e,n,o,i){var a=document.createElement("div");a.id=t;var d=document.createElement("div");d.style.cssText="position:fixed;width:100%;height:100%;z-index:999;top:0;left:0;opacity:0.5;filter:alpha(opacity=50);background-color:#ccc;";var p=document.createElement("div");p.style.cssText="position:relative;left:-50%;margin-top:-25%;background-color:#fff;padding:20px;box-shadow:4px 4px 25px #888;";var s=document.createElement("div");s.style.cssText="z-index:1000;position:fixed;left:50%;top:50%";var u=r(n);return u.style.display="block",u.style.textAlign="right",u.style.marginTop="8px",p.appendChild(c(e)),o&&i&&p.appendChild(l(o,i)),p.appendChild(u),s.appendChild(p),a.appendChild(d),a.appendChild(s),a}(i,d,s,h):a(i,d,s,h,m),C=document.createDocumentFragment();C.appendChild(g),document.body.appendChild(C.cloneNode(!0)),document.getElementById(n).onclick=p,document.getElementById(o).onclick=p}}function u(){var e=document.getElementById(t);null!=e&&e.parentNode.removeChild(e)}function h(e){var t=document.createElement("textarea");return t.innerHTML=e,0===t.childNodes.length?"":t.childNodes[0].nodeValue}document.addEventListener?document.addEventListener("DOMContentLoaded",i):document.attachEvent("onreadystatechange",function(e){"complete"===document.readyState&&i()})}();