const input=document.getElementById('cardImage'); const label=document.getElementById('fileLabel');
if(input) input.addEventListener('change',()=>{if(input.files[0]) label.textContent=input.files[0].name});
