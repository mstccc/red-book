!function(e){var t={};function r(n){if(t[n])return t[n].exports;var o=t[n]={i:n,l:!1,exports:{}};return e[n].call(o.exports,o,o.exports,r),o.l=!0,o.exports}r.m=e,r.c=t,r.d=function(e,t,n){r.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},r.r=function(e){"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},r.t=function(e,t){if(1&t&&(e=r(e)),8&t)return e;if(4&t&&"object"==typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(r.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var o in e)r.d(n,o,function(t){return e[t]}.bind(null,o));return n},r.n=function(e){var t=e&&e.__esModule?function(){return e.default}:function(){return e};return r.d(t,"a",t),t},r.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},r.p="",r(r.s=174)}({0:function(e,t){e.exports={".activate-line":{"":{backgroundColor:["#FFFFFF",0,0,1]}},"@VERSION":2}},1:function(e,t,r){"use strict";function n(e,t,r,n,o,a,i,s,l,u){var c,f="function"==typeof e?e.options:e;if(l){f.components||(f.components={});var p=Object.prototype.hasOwnProperty;for(var d in l)p.call(l,d)&&!p.call(f.components,d)&&(f.components[d]=l[d])}if(u&&((u.beforeCreate||(u.beforeCreate=[])).unshift((function(){this[u.__module]=this})),(f.mixins||(f.mixins=[])).push(u)),t&&(f.render=t,f.staticRenderFns=r,f._compiled=!0),n&&(f.functional=!0),a&&(f._scopeId="data-v-"+a),i?(c=function(e){(e=e||this.$vnode&&this.$vnode.ssrContext||this.parent&&this.parent.$vnode&&this.parent.$vnode.ssrContext)||"undefined"==typeof __VUE_SSR_CONTEXT__||(e=__VUE_SSR_CONTEXT__),o&&o.call(this,e),e&&e._registeredComponents&&e._registeredComponents.add(i)},f._ssrRegister=c):o&&(c=s?function(){o.call(this,this.$root.$options.shadowRoot)}:o),c)if(f.functional){f._injectStyles=c;var y=f.render;f.render=function(e,t){return c.call(t),y(e,t)}}else{var g=f.beforeCreate;f.beforeCreate=g?[].concat(g,c):[c]}return{exports:e,options:f}}r.d(t,"a",(function(){return n}))},122:function(e,t,r){"use strict";r.d(t,"b",(function(){return n})),r.d(t,"c",(function(){return o})),r.d(t,"a",(function(){}));var n=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("scroll-view",{staticStyle:{flexDirection:"column"},attrs:{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true"}},[r("view",{staticClass:["page"]},[r("view",{staticClass:["line"]}),r("scroll-view",{attrs:{scrollY:"true"},on:{scrolltolower:function(t){e.pagingFansList()}}},e._l(e.fansList,(function(t,n){return r("view",{key:n,staticClass:["user-wrapper"]},[r("view",{staticClass:["user-info"]},[r("u-image",{staticClass:["face"],staticStyle:{alignSelf:"center"},attrs:{src:t.face}}),r("u-text",{staticClass:["user-name"],staticStyle:{alignSelf:"center"},appendAsTree:!0,attrs:{append:"tree"}},[e._v(e._s(t.nickname))])],1),t.friend?e._e():r("view",{staticClass:["operator-wrapper"],staticStyle:{width:"140rpx",height:"60rpx",display:"flex",flexDirection:"row",justifyContent:"center",backgroundColor:"#ef274d",borderRadius:"10px",alignSelf:"center"}},[r("u-text",{staticClass:["operator-words"],staticStyle:{alignSelf:"center",color:"#FFFFFF"},appendAsTree:!0,attrs:{append:"tree"},on:{click:function(r){e.followMe(t.fanId)}}},[e._v("\u56de\u7c89")])]),t.friend?r("view",{staticClass:["operator-wrapper"],staticStyle:{width:"140rpx",height:"60rpx",display:"flex",flexDirection:"row",justifyContent:"center",backgroundColor:"#ef274d",borderRadius:"10px",alignSelf:"center",borderWidth:"1px",borderColor:"#ef274d",backgroundColor:"#181b27"}},[r("u-text",{staticClass:["operator-words"],staticStyle:{alignSelf:"center",color:"#ef274d"},appendAsTree:!0,attrs:{append:"tree"},on:{click:function(r){e.cancelFollow(t.fanId)}}},[e._v("\u4e92\u5173")])]):e._e()])})),0)],1)])},o=[]},149:function(e,t,r){"use strict";r.r(t);var n=r(54),o=r.n(n);for(var a in n)"default"!==a&&function(e){r.d(t,e,(function(){return n[e]}))}(a);t.default=o.a},174:function(e,t,r){"use strict";r.r(t);r(2),r(4);var n=r(26);n.default.mpType="page",n.default.route="pages/me/myFans",n.default.el="#root",new Vue(n.default)},2:function(e,t,r){Vue.prototype.__$appStyle__={},Vue.prototype.__merge_style&&Vue.prototype.__merge_style(r(3).default,Vue.prototype.__$appStyle__)},26:function(e,t,r){"use strict";var n=r(122),o=r(52),a=r(1);var i=Object(a.a)(o.default,n.b,n.c,!1,null,null,"73fd18fc",!1,n.a,void 0);(function(e){this.options.style||(this.options.style={}),Vue.prototype.__merge_style&&Vue.prototype.__$appStyle__&&Vue.prototype.__merge_style(Vue.prototype.__$appStyle__,this.options.style),Vue.prototype.__merge_style?Vue.prototype.__merge_style(r(149).default,this.options.style):Object.assign(this.options.style,r(149).default)}).call(i),t.default=i.exports},3:function(e,t,r){"use strict";r.r(t);var n=r(0),o=r.n(n);for(var a in n)"default"!==a&&function(e){r.d(t,e,(function(){return n[e]}))}(a);t.default=o.a},4:function(e,t){if("undefined"==typeof Promise||Promise.prototype.finally||(Promise.prototype.finally=function(e){var t=this.constructor;return this.then((function(r){return t.resolve(e()).then((function(){return r}))}),(function(r){return t.resolve(e()).then((function(){throw r}))}))}),"undefined"!=typeof uni&&uni&&uni.requireGlobal){var r=uni.requireGlobal();ArrayBuffer=r.ArrayBuffer,Int8Array=r.Int8Array,Uint8Array=r.Uint8Array,Uint8ClampedArray=r.Uint8ClampedArray,Int16Array=r.Int16Array,Uint16Array=r.Uint16Array,Int32Array=r.Int32Array,Uint32Array=r.Uint32Array,Float32Array=r.Float32Array,Float64Array=r.Float64Array,BigInt64Array=r.BigInt64Array,BigUint64Array=r.BigUint64Array}},52:function(e,t,r){"use strict";var n=r(53),o=r.n(n);t.default=o.a},53:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;uni.getSystemInfoSync();var n=getApp(),o={components:{},data:function(){return{screenHeight:0,page:0,totalPage:0,fansList:[{fanId:"1001",face:"../../static/face/face-arrow-1.png",nickname:"\u98ce\u95f4\u5f71\u6708",friend:!1},{fanId:"1002",face:"../../static/face/face-arrow-1.png",nickname:"\u6155\u8bfe\u7f51",friend:!0},{fanId:"1003",face:"../../static/face/face-arrow-1.png",nickname:"\u98ce\u95f4\u5f71\u6708",friend:!1}]}},onLoad:function(){this.queryMyFansList(0)},methods:{reFreshList:function(e,t){for(var r=this.fansList,n=0;n<r.length;n++){var o=r[n];o.fanId==e&&(o.friend=t,r.splice(n,1,o))}this.fansList=r},cancelFollow:function(e){var t=this,r=getApp().getUserInfoSession().id,o=n.globalData.serverUrl;uni.request({method:"POST",header:{headerUserId:r,headerUserToken:n.getUserSessionToken()},url:o+"/fans/cancel?myId="+r+"&vlogerId="+e,success:function(r){200==r.data.status?t.reFreshList(e,!1):uni.showToast({title:r.data.msg,icon:"none",duration:3e3})}})},followMe:function(e){var t=this,r=getApp().getUserInfoSession().id,o=n.globalData.serverUrl;uni.request({method:"POST",header:{headerUserId:r,headerUserToken:n.getUserSessionToken()},url:o+"/fans/follow?myId="+r+"&vlogerId="+e,success:function(r){200==r.data.status?t.reFreshList(e,!0):uni.showToast({title:r.data.msg,icon:"none",duration:3e3})}})},queryMyFansList:function(e){var t=this;0==e&&(t.fansList=[]),e+=1;var r=getApp().getUserInfoSession().id,o=n.globalData.serverUrl;uni.request({method:"GET",header:{headerUserId:r,headerUserToken:n.getUserSessionToken()},url:o+"/fans/queryMyFans?myId="+r+"&page="+e+"&pageSize=10",success:function(r){if(200==r.data.status){var n=r.data.data.rows,o=r.data.data.total;t.fansList=t.fansList.concat(n),t.page=e,t.totalPage=o}}})},pagingFansList:function(){this.page>=this.totalPage||this.queryMyFansList(this.page)}}};t.default=o},54:function(e,t){e.exports={".page":{"":{position:["absolute",0,0,0],left:[0,0,0,0],right:[0,0,0,0],top:[0,0,0,0],bottom:[0,0,0,0],backgroundColor:["#181b27",0,0,0]}},".line":{"":{height:["1rpx",0,0,1],backgroundColor:["#393a41",0,0,1],width:["750rpx",0,0,1]}},".place-box":{"":{backgroundColor:["#4a4c52",0,0,2]}},".place-box-touched":{"":{backgroundColor:["#6d6b6b",0,0,3]}},".right-arrow":{"":{width:["32rpx",0,0,4],height:["32rpx",0,0,4],marginLeft:["20rpx",0,0,4]}},".user-wrapper":{"":{paddingLeft:["30rpx",0,0,5],paddingRight:["30rpx",0,0,5],width:["750rpx",0,0,5],height:["120rpx",0,0,5],display:["flex",0,0,5],flexDirection:["row",0,0,5],justifyContent:["space-between",0,0,5],marginTop:["20rpx",0,0,5],marginBottom:["20rpx",0,0,5]}},".user-name":{"":{color:["#FFFFFF",0,0,6],fontSize:["15",0,0,6],marginLeft:["20rpx",0,0,6]}},".operator-wrapper":{"":{width:["140rpx",0,0,7],height:["60rpx",0,0,7],display:["flex",0,0,7],flexDirection:["row",0,0,7],justifyContent:["center",0,0,7],backgroundColor:["#ef274d",0,0,7],borderRadius:["10",0,0,7]}},".operator-words":{"":{color:["#FFFFFF",0,0,8],fontSize:["14",0,0,8]}},".user-info":{"":{display:["flex",0,0,9],flexDirection:["row",0,0,9],justifyContent:["flex-start",0,0,9]}},".face":{"":{width:["110rpx",0,0,10],height:["110rpx",0,0,10],borderRadius:["100rpx",0,0,10],borderWidth:["1",0,0,10],borderColor:["#F1F1F1",0,0,10]}},"@VERSION":2}}});