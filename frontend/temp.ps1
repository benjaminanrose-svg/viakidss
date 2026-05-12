[Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12 
=(Invoke-WebRequest -Uri 'https://viakidss-git-claude-clone-v-127a73-benjaminanrose-svgs-projects.vercel.app/' -UseBasicParsing).Content 
