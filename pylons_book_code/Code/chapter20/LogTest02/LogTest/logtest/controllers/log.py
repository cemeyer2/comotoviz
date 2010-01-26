import logging

from pylons import request, response, session, tmpl_context as c
from pylons.controllers.util import abort, redirect_to

from logtest.lib.base import BaseController, render
#from logtest import model

log = logging.getLogger(__name__)

class LogController(BaseController):

    def index(self):
        log.debug('My first Pylons log message!\n')
        return 'Check the logs!'

    def newlog(self, action):
        log.log(10, 'Another log message')
        return 'Check the logs again!'

