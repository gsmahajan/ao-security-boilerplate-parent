#! /bin/bash
#
#        ______                  _____                __              __
#       /\  _  \                /\  __`\             /\ \      __    /\ \
#       \ \ \L\ \  _____   _____\ \ \/\ \  _ __   ___\ \ \___ /\_\   \_\ \
#        \ \  __ \/\ '__`\/\ '__`\ \ \ \ \/\`'__\/'___\ \  _ `\/\ \  /'_` \
#         \ \ \/\ \ \ \L\ \ \ \L\ \ \ \_\ \ \ \//\ \__/\ \ \ \ \ \ \/\ \L\ \
#          \ \_\ \_\ \ ,__/\ \ ,__/\ \_____\ \_\\ \____\\ \_\ \_\ \_\ \___,_\
#           \/_/\/_/\ \ \/  \ \ \/  \/_____/\/_/ \/____/ \/_/\/_/\/_/\/__,_ /
#                    \ \_\   \ \_\
#                     \/_/    \/_/
#
#
#
# Credit - girish.mahajan@accionlabs.com

function speak {
	echo "$1" | sed -e 's/_/ /g' -e 's/://g' > /d/loud.txt
	start /d/loud.vbs
}


function display_banner {
	which filget >> /dev/null 2>&1
	[ 0 == $? ] && 	figlet "AppOrchid $(date +%Y)" || log_info "AppOrchid Inc - 2018 - author: girish.mahajan@accionlabs.com";
} 

function log_warn {
        echo -e `date +%d/%m/%Y-%H:%M:%S ` "\033[33m# WARNING # " $1 "\033[0m" | speak "$1";
}

function log_info {
         echo -e `date +%d/%m/%Y-%H:%M:%S ` "\033[32m# INFO    # " $1 "\033[0m" | tee -a "$LOGFILE";
}

function log_debug {
         echo -e `date +%d/%m/%Y-%H:%M:%S ` "\033[32m# INFO    # " $1 "\033[0m" | tee -a $LOGFILE;
}

function log_error {
        echo -e `date +%d/%m/%Y-%H:%M:%S ` "\033[31m# ERROR   # " $1 "\033[0m" | speak "$1";
}

display_banner
