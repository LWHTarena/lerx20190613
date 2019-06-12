jQuery.fn.extend({
	autoimg: function(a) {
		
		var cur, l, las, iCount;

		$(this).addClass("rootDiv");
		var liimg = $(this).find("ul").eq(0).find("li");
		var litxt = $(this).find("ul").eq(1).find("li");
		var linum = $(this).find("ul").eq(2).find("li");
		liimg.addClass("li_top_image_lerx20151222");
		litxt.addClass("li_footer_title_lerx20151222");
		linum.addClass("li_footer_number_lerx20151222");
		if (a.tipsMod == 1){
			$(this).find("div").eq(0).addClass("div_footer_luara_lerx20160324");
		}else{
			$(this).find("div").eq(0).addClass("div_footer_luara_lerx20151222");
		}
		
		
		liimg.find("img").each(function() {
			if (a.smartDraw){
				drawImageSmart(this, a.width, a.height);
			}else{
				drawImageBrutal(this, a.width, a.height);
			}
			
		});
		
		function drawImageBrutal(imgD, fitWidth, fitHeight) {
			var image = new Image();
			image.src = imgD.src;
			imgD.width = fitWidth;
			imgD.height = fitHeight;
		}

		function drawImageSmart(imgD, fitWidth, fitHeight) {
			var image = new Image();
			image.src = imgD.src;
			if (image.width > 0 && image.height > 0) {
				if (image.width / image.height >= fitWidth / fitHeight) {
					if (image.width > fitWidth) {
						imgD.width = fitWidth;
						imgD.height = (image.height * fitWidth) / image.width;
					} else {
						imgD.width = image.width;
						imgD.height = image.height;
					}
				} else {
					if (image.height > fitHeight) {
						imgD.height = fitHeight;
						imgD.width = (image.width * fitHeight) / image.height;
					} else {
						imgD.width = image.width;
						imgD.height = image.height;
					}
				}
			}
		}

		cur = 0;
		l = liimg.length;
		liimg.eq(cur).fadeIn();
		//$(this).css("width", a.width);
		$(this).find("ul").eq(0).css("height", a.height);
		$(this).find("ul").eq(1).css("height", 18);
		$(this).find("ul").eq(2).css("height", 18);

		if (a.mode == 0) {

			/*
			for (i = 0; i < l; i++) { 
				$(this).find("ul").eq(2).append("<li class='li_footer_number_lerx20151222'><a href='javascript:;'>"+(i+1)+"</a></li>");
			}
			*/

			
			$(this).find("div").eq(0).css("background", "#343434");
			$(this).find("div").eq(0).css("height", 26);

			$(this).find("ul").eq(1).addClass("ultxt_lerx20151222");
			$(this).find("ul").eq(2).addClass("ulnum_lerx20151222");
			litxt.attr("position", 'absolute');
			litxt.hide();
			litxt.eq(cur).fadeIn(0);
			linum.addClass("li_footer_number_span_lerx20151222");
			linum.eq(cur).addClass("li_num_striking_lerx20151222");
		} else {
			$(this).find("ul").eq(2).hide();
			litxt.eq(cur).addClass("li_txt_striking_lerx20151222");

		}
		colorCur(litxt, cur);
		focusCur(litxt, cur);
		linum.find("a").css("color", a.colorNum);

		run();

		function run() {
			iCount = setInterval(function() {
				fad(cur, l);
			}, a.interval);
		}

		function fad(current, len) {
			current++;
			if (current == len) {
				current = 0;
			}
			las = current - 1;
			if (las < 0) {
				las = len - 1;
			}
			liimg.eq(las).fadeOut();
			liimg.eq(current).fadeIn(a.speed);

			if (a.mode == 0) {
				swi(litxt, linum, current);
			} else {
				striking(litxt, current);
			}
			cur = current;
		}

		function focusCur(txt, current) {
			txt.eq(current).css("color", a.colorFocus);
			txt.eq(current).find("a").css("color", a.colorFocus);
		}

		function colorCur(txt, current) {
			txt.css("color", a.colorTitle);
			txt.find("a").css("color", a.colorTitle);
		}

		function striking(txt, cur) {
			txt.removeClass();
			colorCur(txt, cur);
			focusCur(txt, cur);
			txt.addClass("li_footer_title_lerx20151222");
			txt.eq(cur).addClass("li_txt_striking_lerx20151222");
		}

		function swi(txt, num, cur) {
			txt.hide();
			txt.eq(cur).fadeIn(0);
			focusCur(txt, cur);
			num.removeClass("li_num_striking_lerx20151222");
			num.eq(cur).addClass("li_num_striking_lerx20151222");
		}

		function sel(current, ind) {
			if (current != ind) {
				liimg.eq(current).fadeOut();
				if (a.mode == 0) {
					swi(litxt, linum, ind);
				} else {
					striking(litxt, ind);
				}
				liimg.eq(ind).fadeIn(0);
				cur = ind;
			}
		}

		linum.mouseover(function() {
			sel(cur, $(this).index());
			clearInterval(iCount);
		});

		linum.mouseleave(function() {
			run();
		});

		litxt.mouseover(function() {
			if (a.mode == 1) {
				sel(cur, $(this).index());
				clearInterval(iCount);
			}
		});

		litxt.mouseleave(function() {
			if (a.mode == 1) {
				run();
			}
		});
	}
});