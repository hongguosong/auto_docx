(function($) {
  'use strict';
  $(function() {
    $(".nav-settings").on("click", function() {
      $("#right-sidebar").toggleClass("open");
    });
    $(".settings-close").on("click", function() {
      $("#right-sidebar,#theme-settings").removeClass("open");
    });

    $("#settings-trigger").on("click" , function(){
      $("#theme-settings").toggleClass("open");
    });


    //background constants
    var navbar_classes = "navbar-danger navbar-success navbar-warning navbar-dark navbar-light navbar-primary navbar-info navbar-pink";
    var sidebar_classes = "sidebar-light sidebar-dark";
    var $body = $("body");

    //sidebar backgrounds
    $("#sidebar-light-theme").on("click" , function(){
      $body.removeClass(sidebar_classes);
      $body.addClass("sidebar-light");
      $(".sidebar-bg-options").removeClass("selected");
      $(this).addClass("selected");
      if(window.localStorage){
        window.localStorage.setItem("sidebarTheme", "light");
      }
    });
    $("#sidebar-dark-theme").on("click" , function(){
      $body.removeClass(sidebar_classes);
      $body.addClass("sidebar-dark");
      $(".sidebar-bg-options").removeClass("selected");
      $(this).addClass("selected");
      if(window.localStorage){
        window.localStorage.setItem("sidebarTheme", "dark");
      }
    });
    if(window.localStorage){
        var sidebarTheme = window.localStorage.getItem("sidebarTheme");
        if(sidebarTheme == "light"){
            $body.removeClass(sidebar_classes);
            $body.addClass("sidebar-light");
            $(".sidebar-bg-options").removeClass("selected");
            $(this).addClass("selected");
        }else if(sidebarTheme == "dark"){
            $body.removeClass(sidebar_classes);
            $body.addClass("sidebar-dark");
            $(".sidebar-bg-options").removeClass("selected");
            $(this).addClass("selected");
        }
    }

    //Navbar Backgrounds
    $(".tiles.primary").on("click" , function(){
      $(".navbar").removeClass(navbar_classes);
      $(".navbar").addClass("navbar-primary");
      $(".tiles").removeClass("selected");
      $(this).addClass("selected");
      if(window.localStorage){
        window.localStorage.setItem("navbarTheme", "primary");
      }
    });
    $(".tiles.success").on("click" , function(){
      $(".navbar").removeClass(navbar_classes);
      $(".navbar").addClass("navbar-success");
      $(".tiles").removeClass("selected");
      $(this).addClass("selected");
      if(window.localStorage){
        window.localStorage.setItem("navbarTheme", "success");
      }
    });
    $(".tiles.warning").on("click" , function(){
      $(".navbar").removeClass(navbar_classes);
      $(".navbar").addClass("navbar-warning");
      $(".tiles").removeClass("selected");
      $(this).addClass("selected");
      if(window.localStorage){
        window.localStorage.setItem("navbarTheme", "warning");
      }
    });
    $(".tiles.danger").on("click" , function(){
      $(".navbar").removeClass(navbar_classes);
      $(".navbar").addClass("navbar-danger");
      $(".tiles").removeClass("selected");
      $(this).addClass("selected");
      if(window.localStorage){
        window.localStorage.setItem("navbarTheme", "danger");
      }
    });
    $(".tiles.light").on("click" , function(){
      $(".navbar").removeClass(navbar_classes);
      $(".navbar").addClass("navbar-light");
      $(".tiles").removeClass("selected");
      $(this).addClass("selected");
      if(window.localStorage){
        window.localStorage.setItem("navbarTheme", "light");
      }
    });
    $(".tiles.info").on("click" , function(){
      $(".navbar").removeClass(navbar_classes);
      $(".navbar").addClass("navbar-info");
      $(".tiles").removeClass("selected");
      $(this).addClass("selected");
      if(window.localStorage){
        window.localStorage.setItem("navbarTheme", "info");
      }
    });
    $(".tiles.dark").on("click" , function(){
      $(".navbar").removeClass(navbar_classes);
      $(".navbar").addClass("navbar-dark");
      $(".tiles").removeClass("selected");
      $(this).addClass("selected");
      if(window.localStorage){
        window.localStorage.setItem("navbarTheme", "dark");
      }
    });
    $(".tiles.default").on("click" , function(){
      $(".navbar").removeClass(navbar_classes);
      $(".tiles").removeClass("selected");
      $(this).addClass("selected");
      if(window.localStorage){
        window.localStorage.setItem("navbarTheme", "default");
      }
    });

    if(window.localStorage){
      var navbarTheme = window.localStorage.getItem("navbarTheme");
      if(navbarTheme == "primary"){
        $(".navbar").removeClass(navbar_classes);
        $(".navbar").addClass("navbar-primary");
        $(".tiles").removeClass("selected");
        $(this).addClass("selected");
      } else if(navbarTheme == "success"){
        $(".navbar").removeClass(navbar_classes);
        $(".navbar").addClass("navbar-success");
        $(".tiles").removeClass("selected");
        $(this).addClass("selected");
      } else if(navbarTheme == "warning"){
        $(".navbar").removeClass(navbar_classes);
        $(".navbar").addClass("navbar-warning");
        $(".tiles").removeClass("selected");
        $(this).addClass("selected");
      } else if(navbarTheme == "danger"){
        $(".navbar").removeClass(navbar_classes);
        $(".navbar").addClass("navbar-danger");
        $(".tiles").removeClass("selected");
        $(this).addClass("selected");
      } else if(navbarTheme == "light"){
        $(".navbar").removeClass(navbar_classes);
        $(".navbar").addClass("navbar-light");
        $(".tiles").removeClass("selected");
        $(this).addClass("selected");
      } else if(navbarTheme == "info"){
        $(".navbar").removeClass(navbar_classes);
        $(".navbar").addClass("navbar-info");
        $(".tiles").removeClass("selected");
        $(this).addClass("selected");
      } else if(navbarTheme == "dark"){
        $(".navbar").removeClass(navbar_classes);
        $(".navbar").addClass("navbar-dark");
        $(".tiles").removeClass("selected");
        $(this).addClass("selected");
      } else if(navbarTheme == "default"){
        $(".navbar").removeClass(navbar_classes);
        $(".tiles").removeClass("selected");
        $(this).addClass("selected");
      }
    }
  });
})(jQuery);
