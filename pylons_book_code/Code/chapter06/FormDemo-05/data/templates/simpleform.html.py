from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 4
_modified_time = 1224670514.082211
_template_filename='/media/disk/Pylons Book/Code/chapter06/FormDemo/formdemo/templates/simpleform.html'
_template_uri='/simpleform.html'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='utf-8'
from webhelpers.html import escape
_exports = []


def _mako_get_namespace(context, name):
    try:
        return context.namespaces[(__name__, name)]
    except KeyError:
        _mako_generate_namespaces(context)
        return context.namespaces[(__name__, name)]
def _mako_generate_namespaces(context):
    pass
def _mako_inherit(template, context):
    _mako_generate_namespaces(context)
    return runtime._inherit_from(context, '/base.html', _template_uri)
def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        h = context.get('h', UNDEFINED)
        c = context.get('c', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 1
        __M_writer(u'\n<h1>Enter Your E-mail Address</h1>\n\n')
        # SOURCE LINE 4
        __M_writer(escape(h.form(h.url_for(controller='formtest', action='submit'), method='get')))
        __M_writer(u'\n')
        # SOURCE LINE 5
        if c.email_msg:
            # SOURCE LINE 6
            __M_writer(u'    <span class="error-message">')
            __M_writer(escape(c.email_msg))
            __M_writer(u'</span><br />\n')
        # SOURCE LINE 8
        __M_writer(u'E-mail Address: ')
        __M_writer(escape(h.text('email', value=c.email_value)))
        __M_writer(u'\n                ')
        # SOURCE LINE 9
        __M_writer(escape(h.submit('submit', 'Submit')))
        __M_writer(u'\n')
        # SOURCE LINE 10
        __M_writer(escape(h.end_form()))
        __M_writer(u'\n</form>\n\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


