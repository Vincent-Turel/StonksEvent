#!/usr/bin/env bash

# case "$(uname -s)" in

#    CYGWIN*|MINGW*|MSYS*)
#      csc -out:MailService.exe -recurse:*.cs;;


#    *)
#      mcs src/*.cs -pkg:wcf -out:MailService.exe;;

# esac

dotnet build MailService.sln