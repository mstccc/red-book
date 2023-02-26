
var isReady=false;var onReadyCallbacks=[];
var isServiceReady=false;var onServiceReadyCallbacks=[];
var __uniConfig = {"pages":["pages/index/index","pages/friend/friend","pages/vlog/vlog","pages/publish/publish","pages/me/me","pages/me/vlogerInfo","pages/message/message","pages/publish/preview","pages/search/search","pages/search/searchList","pages/qrcode/qrcode","pages/loginRegist/loginRegist","pages/me/myBackImg","pages/me/myFace","pages/me/myInfo","pages/me/modifyNickname","pages/me/modifyIMoocNum","pages/me/modifyDesc","pages/me/modifySex","pages/me/modifyBirthday","pages/me/modifyLocation","pages/me/chooseProvince","pages/me/chooseCity","pages/me/settings","pages/me/myFans","pages/me/myFollows"],"window":{"navigationBarBackgroundColor":"#000000","navigationBarTextStyle":"white","navigationBarTitleText":"uni-app","backgroundColor":"#000000"},"tabBar":{"color":"#999696","selectedColor":"#FFFFFF","borderStyle":"#1c1b1b","backgroundColor":"#0e0d0d","fontSize":"16px","list":[{"pagePath":"pages/index/index","text":"首页"},{"pagePath":"pages/friend/friend","text":"朋友"},{"pagePath":"pages/message/message","text":"消息"},{"pagePath":"pages/me/me","text":"我"}],"midButton":{"height":"38px","iconWidth":"40px","iconPath":"static/images/douyin.png"}},"nvueCompiler":"uni-app","nvueStyleCompiler":"uni-app","renderer":"auto","splashscreen":{"alwaysShowBeforeRender":true,"autoclose":false},"appname":"仿抖音APP","compilerVersion":"3.6.3","entryPagePath":"pages/index/index","networkTimeout":{"request":60000,"connectSocket":60000,"uploadFile":60000,"downloadFile":60000}};
var __uniRoutes = [{"path":"/pages/index/index","meta":{"isQuit":true,"isNVue":true,"isTabBar":true},"window":{"enablePullDownRefresh":true,"titleNView":false}},{"path":"/pages/friend/friend","meta":{"isQuit":true,"isNVue":true,"isTabBar":true},"window":{"titleNView":false}},{"path":"/pages/vlog/vlog","meta":{"isNVue":true},"window":{"titleNView":false}},{"path":"/pages/publish/publish","meta":{"isNVue":true},"window":{"navigationBarTitleText":"发布视频"}},{"path":"/pages/me/me","meta":{"isQuit":true,"isNVue":true,"isTabBar":true},"window":{"titleNView":false}},{"path":"/pages/me/vlogerInfo","meta":{"isNVue":true},"window":{"titleNView":false}},{"path":"/pages/message/message","meta":{"isQuit":true,"isNVue":true,"isTabBar":true},"window":{"navigationBarTitleText":"消息","navigationBarBackgroundColor":"#181b27"}},{"path":"/pages/publish/preview","meta":{"isNVue":true},"window":{"navigationBarTitleText":"预览视频","enablePullDownRefresh":false}},{"path":"/pages/search/search","meta":{},"window":{"titleNView":false}},{"path":"/pages/search/searchList","meta":{"isNVue":true},"window":{"titleNView":false}},{"path":"/pages/qrcode/qrcode","meta":{},"window":{"titleNView":false,"navigationBarTextStyle":"black"}},{"path":"/pages/loginRegist/loginRegist","meta":{},"window":{"titleNView":false,"navigationBarTextStyle":"black"}},{"path":"/pages/me/myBackImg","meta":{},"window":{"titleNView":false}},{"path":"/pages/me/myFace","meta":{},"window":{"titleNView":false}},{"path":"/pages/me/myInfo","meta":{"isNVue":true},"window":{"navigationBarTitleText":"修改个人资料","navigationBarBackgroundColor":"#181b27"}},{"path":"/pages/me/modifyNickname","meta":{"isNVue":true},"window":{"navigationBarTitleText":"修改昵称","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27","titleNView":{"buttons":[{"text":"保存","color":"#ef274d","width":"60px","fontSize":"15px","fontWeight":"bold"}]}}},{"path":"/pages/me/modifyIMoocNum","meta":{"isNVue":true},"window":{"navigationBarTitleText":"修改慕课号","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27","titleNView":{"buttons":[{"text":"保存","color":"#ef274d","width":"60px","fontSize":"15px","fontWeight":"bold"}]}}},{"path":"/pages/me/modifyDesc","meta":{"isNVue":true},"window":{"navigationBarTitleText":"修改简介","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27","titleNView":{"buttons":[{"text":"保存","color":"#ef274d","width":"60px","fontSize":"15px","fontWeight":"bold"}]}}},{"path":"/pages/me/modifySex","meta":{"isNVue":true},"window":{"navigationBarTitleText":"修改性别","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27","titleNView":{"buttons":[{"text":"保存","color":"#ef274d","width":"60px","fontSize":"15px","fontWeight":"bold"}]}}},{"path":"/pages/me/modifyBirthday","meta":{"isNVue":true},"window":{"navigationBarTitleText":"修改生日","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27","titleNView":{"buttons":[{"text":"保存","color":"#ef274d","width":"60px","fontSize":"15px","fontWeight":"bold"}]}}},{"path":"/pages/me/modifyLocation","meta":{"isNVue":true},"window":{"navigationBarTitleText":"修改所在地","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27","titleNView":{"buttons":[{"text":"保存","color":"#ef274d","width":"60px","fontSize":"15px","fontWeight":"bold"}]}}},{"path":"/pages/me/chooseProvince","meta":{"isNVue":true},"window":{"navigationBarTitleText":"选择省份","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27"}},{"path":"/pages/me/chooseCity","meta":{"isNVue":true},"window":{"navigationBarTitleText":"选择地区","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27"}},{"path":"/pages/me/settings","meta":{"isNVue":true},"window":{"navigationBarTitleText":"设置","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27"}},{"path":"/pages/me/myFans","meta":{"isNVue":true},"window":{"navigationBarTitleText":"我的粉丝","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27"}},{"path":"/pages/me/myFollows","meta":{"isNVue":true},"window":{"navigationBarTitleText":"我的关注","navigationBarTextStyle":"white","navigationBarBackgroundColor":"#181b27"}}];
__uniConfig.onReady=function(callback){if(__uniConfig.ready){callback()}else{onReadyCallbacks.push(callback)}};Object.defineProperty(__uniConfig,"ready",{get:function(){return isReady},set:function(val){isReady=val;if(!isReady){return}const callbacks=onReadyCallbacks.slice(0);onReadyCallbacks.length=0;callbacks.forEach(function(callback){callback()})}});
__uniConfig.onServiceReady=function(callback){if(__uniConfig.serviceReady){callback()}else{onServiceReadyCallbacks.push(callback)}};Object.defineProperty(__uniConfig,"serviceReady",{get:function(){return isServiceReady},set:function(val){isServiceReady=val;if(!isServiceReady){return}const callbacks=onServiceReadyCallbacks.slice(0);onServiceReadyCallbacks.length=0;callbacks.forEach(function(callback){callback()})}});
service.register("uni-app-config",{create(a,b,c){if(!__uniConfig.viewport){var d=b.weex.config.env.scale,e=b.weex.config.env.deviceWidth,f=Math.ceil(e/d);Object.assign(__uniConfig,{viewport:f,defaultFontSize:Math.round(f/20)})}return{instance:{__uniConfig:__uniConfig,__uniRoutes:__uniRoutes,global:void 0,window:void 0,document:void 0,frames:void 0,self:void 0,location:void 0,navigator:void 0,localStorage:void 0,history:void 0,Caches:void 0,screen:void 0,alert:void 0,confirm:void 0,prompt:void 0,fetch:void 0,XMLHttpRequest:void 0,WebSocket:void 0,webkit:void 0,print:void 0}}}});