<html>
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpRequestAttachment" -->
<head>
<meta charset="UTF-8">
<style>
:root{--l:#008000;--v:#c7254e;--k:#0066cc}
*{box-sizing:border-box}
body{font:14px/1.5 -apple-system,BlinkMacSystemFont,"Segoe UI",sans-serif;margin:12px;color:#333}
h4{margin:16px 0 8px;font-size:13px;color:#666}
pre{margin:0;white-space:pre-wrap;word-break:break-all}
code{font-family:ui-monospace,monospace;font-size:13px;background:#f5f5f5;padding:2px 6px;border-radius:4px}
pre code{display:block;padding:10px;background:#f5f5f5;border-radius:4px;border:1px solid #e0e0e0}
.hl-method,.hl-header-name{color:var(--l);font-weight:600}
.hl-url,.hl-json-string,.hl-header-value{color:var(--v)}
.hl-json-key{color:var(--k)}
</style>
</head>
<body>
<div><pre><code><#if data.method??><span class="hl-method">${data.method}</span><#else>GET</#if>: <#if data.url??><span class="hl-url">${data.url}</span><#else>Unknown</#if></code></pre></div>
<#if data.body??>
<h4>Body</h4>
<div><pre><code class="hl-json">${data.body}</code></pre></div>
</#if>
<#if (data.headers)?has_content>
<h4>Headers</h4>
<#list data.headers as name, value>
<div><pre><code><span class="hl-header-name">${name}</span>: <span class="hl-header-value">${value}</span></code></pre></div>
</#list>
</#if>
<#if (data.cookies)?has_content>
<h4>Cookies</h4>
<#list data.cookies as name, value>
<div><pre><code><span class="hl-header-name">${name}</span>: <span class="hl-header-value">${value}</span></code></pre></div>
</#list>
</#if>
<#if data.curl??>
<h4>Curl</h4>
<div><pre><code class="hl-curl">${data.curl}</code></pre></div>
</#if>
<script>
(function(){
var e=function(s){return(s+'').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;')};
function json(el){
  var t=el.textContent,i=0,h='',c,s,j;
  for(;i<t.length;i++)
    if(t[i]==='"'){
      s=i++;
      while(i<t.length){if(t[i]==='\\')i+=2;else if(t[i]==='"'){i++;break;}else i++}
      j=i;while(j<t.length&&/\s/.test(t[j]))j++;
      h+='<span class="hl-'+(t[j]===':'?'json-key':'json-string')+'">'+e(t.slice(s,i))+'</span>';
      i--;
    }else h+=e(t[i]);
  el.innerHTML=h;
}
function curl(el){
  var h=el.textContent;
  el.innerHTML=h.replace(/\b(POST|GET|PUT|DELETE|PATCH|HEAD)\b/g,'<span class="hl-method">$1</span>').replace(/'([^'\\]|\\.)*'|"([^"\\]|\\.)*"/g,function(m){return'<span class="hl-url">'+e(m)+'</span>'});
}
var codes=document.body.querySelectorAll('code.hl-json, code.hl-curl');
for(var i=0;i<codes.length;i++) codes[i].classList.contains('hl-curl')?curl(codes[i]):(codes[i].textContent.trim().match(/^[{[]/)&&json(codes[i]));
})();
</script>
</body>
</html>