(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{22:function(e,t,a){e.exports=a(59)},28:function(e,t,a){},59:function(e,t,a){"use strict";a.r(t);var n=a(0),o=a.n(n),i=a(11),c=a.n(i),l=(a(28),a(3)),r=a(4),s=a(7),u=a(5),d=a(6),h=a(2),g=a(8),p=a.n(g),f=function(e){function t(){var e,a;Object(l.a)(this,t);for(var n=arguments.length,o=new Array(n),i=0;i<n;i++)o[i]=arguments[i];return(a=Object(s.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(o)))).handleDelete=function(e){if(window.confirm("delete it?")){var t=a.props.sign;a.props.delSign(t.signId)}},a}return Object(d.a)(t,e),Object(r.a)(t,[{key:"render",value:function(){var e=this.props.sign,t="data:image/png;base64,"+e.encImg;return o.a.createElement("span",null,o.a.createElement("img",{alt:"sign",style:{width:"200px",border:"solid",margin:"10px"},src:t,id:e.signId}),o.a.createElement("button",{onClick:this.handleDelete},"X"))}}]),t}(n.Component),v=function(e){function t(){return Object(l.a)(this,t),Object(s.a)(this,Object(u.a)(t).apply(this,arguments))}return Object(d.a)(t,e),Object(r.a)(t,[{key:"render",value:function(){var e=this.props,t=e.signs,a=e.delSign,n=t.map(function(e){return o.a.createElement(f,{sign:e,key:e.signId,delSign:a})});return o.a.createElement("div",null,n)}}]),t}(n.Component);v.defaultProps={signs:[]};var m=v,b=function(e){function t(){var e,a;Object(l.a)(this,t);for(var n=arguments.length,o=new Array(n),i=0;i<n;i++)o[i]=arguments[i];return(a=Object(s.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(o)))).ctx=null,a.isDraw=!1,a.currP=null,a.width="3",a.color="#000000",a.handleMouseDown=function(e){0===e.button&&(e.preventDefault(),a.ctx.beginPath(),a.isDraw=!0)},a.handleMouseMove=function(e){var t=e.nativeEvent;e.preventDefault(),a.currP={X:t.offsetX,Y:t.offsetY},a.isDraw&&a.draw_line(a.currP)},a.draw_line=function(e){a.ctx.lineWidth=a.width,a.ctx.lineCap="round",a.ctx.lineTo(e.X,e.Y),a.ctx.moveTo(e.X,e.Y),a.ctx.strokeStyle=a.color,a.ctx.stroke()},a.handleMouseUp=function(e){e.preventDefault(),a.isDraw=!1},a.handleMouseLeave=function(e){a.isDraw=!1},a.handleTouchStart=function(e){e.preventDefault(),a.ctx.beginPath()},a.handleTouchMove=function(e){var t=e.originalEvent;e.preventDefault(),a.currP={X:t.touches[0].pageX-a.canvas.offsetLeft,Y:t.touches[0].pageY-a.canvas.offsetTop},a.draw_line(a.currP)},a.handleTouchEnd=function(e){e.preventDefault()},a.handleClearCanvas=function(e){a.ctx.clearRect(0,0,a.canvas.width,a.canvas.height),a.ctx.beginPath()},a.saveImage=function(e){var t=a.canvas;localStorage.setItem("imgCanvas",t.toDataURL("image/png"));var n=new FormData;n.append("data",t.toDataURL("image/png")),p.a.post("http://localhost:8080/uploadsign",n).then(function(e){alert("\uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4"),a.handleClearCanvas(),a.props.closeModal()}).catch(function(e){alert("Request failed: "+e)})},a}return Object(d.a)(t,e),Object(r.a)(t,[{key:"componentDidMount",value:function(){this.ctx=this.canvas.getContext("2d")}},{key:"render",value:function(){var e=this;return o.a.createElement("div",null,o.a.createElement("div",{align:"center"},o.a.createElement("canvas",{ref:function(t){return e.canvas=t},width:"320",height:"320",style:{border:"1px solid #000000"},onMouseDown:this.handleMouseDown,onMouseMove:this.handleMouseMove,onMouseUp:this.handleMouseUp,onMouseLeave:this.handleMouseLeave,onTouchStart:this.handleTouchStart,onTouchMove:this.handleTouchMove,onTouchEnd:this.handleTouchEnd},"Canvas not supported")),o.a.createElement("div",{align:"center"},o.a.createElement("button",{id:"btnClea",onClick:this.handleClearCanvas},"Clear"),o.a.createElement("button",{id:"btnSave",onClick:this.saveImage},"\uc800\uc7a5")))}}]),t}(n.Component),w=function(e){function t(){var e,a;Object(l.a)(this,t);for(var n=arguments.length,o=new Array(n),i=0;i<n;i++)o[i]=arguments[i];return(a=Object(s.a)(this,(e=Object(u.a)(t)).call.apply(e,[this].concat(o)))).ctx=null,a.isDraw=!1,a.currP=null,a.width="3",a.color="#000000",a.handleClearCanvas=function(e){a.ctx.clearRect(0,0,a.canvas.width,a.canvas.height),a.ctx.beginPath()},a.saveImage=function(e){a.filterImg();var t=a.canvas;localStorage.setItem("imgCanvas",t.toDataURL("image/png"));var n=new FormData;n.append("data",t.toDataURL("image/png")),p.a.post("http://localhost:8080/uploadsign",n).then(function(e){alert("\uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4"),a.handleClearCanvas(),a.props.closeModal()}).catch(function(e){alert("Request failed: "+e)})},a.filterImg=function(){var e=a.ctx.getImageData(0,0,a.canvas.width,a.canvas.height),t=a.evicRed(e);a.ctx.putImageData(t,0,0)},a.handleChange=function(e){var t=e.target.files[0],n=new FileReader;n.onload=function(e){var t=new Image;t.src=e.target.result,t.onload=function(){a.drawImageData(t)}},n.readAsDataURL(t)},a.drawImageData=function(e){var t=a.canvas;e.height*=t.offsetWidth/e.width,e.width=t.offsetWidth,e.height>t.offsetHeight&&(e.width*=t.offsetHeight/e.height,e.height=t.offsetHeight),a.ctx.drawImage(e,0,0,e.width,e.height),console.log(a.ctx.getImageData(0,0,t.width,t.height))},a}return Object(d.a)(t,e),Object(r.a)(t,[{key:"componentDidMount",value:function(){this.ctx=this.canvas.getContext("2d")}},{key:"evicRed",value:function(e){for(var t=e.data,a=0;a<e.data.length;a+=4)t[a]>240&&t[a+1]>240&&t[a+2]>240?t[a+3]=0:t[a]>150&&t[a+1]<150&&t[a+2]<150||(t[a+3]=0);return e}},{key:"render",value:function(){var e=this;return o.a.createElement("div",null,o.a.createElement("div",{align:"center"},o.a.createElement("canvas",{ref:function(t){return e.canvas=t},width:"320",height:"320",style:{border:"1px solid #000000"}},"Canvas not supported")),o.a.createElement("div",{align:"center"},o.a.createElement("input",{type:"file",accept:"image/*",onChange:this.handleChange}),o.a.createElement("button",{id:"btnSave",onClick:this.saveImage},"\uc800\uc7a5")))}}]),t}(n.Component),M=a(12),O=a.n(M),C={content:{top:"50%",left:"50%",right:"auto",bottom:"auto",marginRight:"-50%",transform:"translate(-50%, -50%)"}};O.a.setAppElement("#root");var E=function(e){function t(){var e;return Object(l.a)(this,t),(e=Object(s.a)(this,Object(u.a)(t).call(this))).closeModal=function(){e.getImages(),e.setState({modalIsOpen:!1,modalComponent:""})},e.getImages=function(){p.a.get("http://localhost:8080/images").then(function(t){var a=t.data;e.setState({signs:a})}).catch(function(e){console.log(e)})},e.delSign=function(t){var a=new FormData;a.append("imgId",t),p.a.post("http://localhost:8080/delsign",a).then(function(t){alert("successfully deleted!"),e.getImages()}).catch(function(e){alert("Request failed: "+e)})},e.state={modalIsOpen:!1,modalComponent:"",signs:[]},e.openSignModal=e.openSignModal.bind(Object(h.a)(Object(h.a)(e))),e.openStampModal=e.openStampModal.bind(Object(h.a)(Object(h.a)(e))),e.afterOpenModal=e.afterOpenModal.bind(Object(h.a)(Object(h.a)(e))),e.closeModal=e.closeModal.bind(Object(h.a)(Object(h.a)(e))),e}return Object(d.a)(t,e),Object(r.a)(t,[{key:"openSignModal",value:function(){this.setState({modalIsOpen:!0,modalComponent:"sign"})}},{key:"openStampModal",value:function(){this.setState({modalIsOpen:!0,modalComponent:"stamp"})}},{key:"afterOpenModal",value:function(){}},{key:"componentDidMount",value:function(){console.log("did mount!"),this.getImages()}},{key:"render",value:function(){return o.a.createElement("div",null,o.a.createElement("h1",null,"\uc0ac\uc778 / \ub3c4\uc7a5"),o.a.createElement("button",{onClick:this.openSignModal},"\uc0ac\uc778\ucd94\uac00"),o.a.createElement("button",{onClick:this.openStampModal},"\ub3c4\uc7a5\ucd94\uac00"),o.a.createElement(m,{signs:this.state.signs,delSign:this.delSign}),o.a.createElement(O.a,{isOpen:this.state.modalIsOpen,onAfterOpen:this.afterOpenModal,onRequestClose:this.closeModal,style:C,contentLabel:"Example Modal"},"sign"===this.state.modalComponent?o.a.createElement(b,{closeModal:this.closeModal}):o.a.createElement(w,{closeModal:this.closeModal})))}}]),t}(n.Component);Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));c.a.render(o.a.createElement(E,null),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then(function(e){e.unregister()})}},[[22,2,1]]]);
//# sourceMappingURL=main.591dde31.chunk.js.map