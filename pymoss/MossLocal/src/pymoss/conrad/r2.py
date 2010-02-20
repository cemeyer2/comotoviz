#!/usr/bin/env python

from pymoss.model import *
from sqlalchemy.orm.exc import NoResultFound
import BeautifulSoup
import StringIO
import cgi
import elixir
import os
import random
import random
import re
import shutil
import subprocess
import tempfile
#elixir.metadata.bind = "sqlite:///db"
elixir.metadata.bind = "mysql://conrad:conrad@localhost/conrad"
#elixir.metadata.bind.echo = True

def main():
	elixir.setup_all(True)

	quadtree = Assignment.get(('cs225', 'imagemanipulation'))
	ae = quadtree.analysis
	me = ae.mossAnalysis
	print ae, me

	r = quadtree.report = Report(assignment=quadtree, mossReport=MossReport())
	mr = r.mossReport
	outputPage = open(os.path.join(me.workDirectory, 'report.html'), 'w')
	outputPage.write('<h1>Results for %s</h1>' % cgi.escape(repr(ae.assignment)))

	outputPage.write('<h2>Solution matches</h2>')
	outputPage.write('<table border="1">')
	# should use xor
	for match in filter(lambda x: isinstance(x.submission1, SolutionSubmission) or isinstance(x.submission2, SolutionSubmission),
			    me.matches):
		outputPage.write('<tr>')
		outputPage.write('<td>%s (%s)</td>' % (cgi.escape(repr(match.submission1)), cgi.escape(str(match.score1))))
		outputPage.write('<td>%s (%s)</td>' % (cgi.escape(repr(match.submission2)), cgi.escape(str(match.score2))))
		outputPage.write('<td><a href="%s">details</a></td>' % cgi.escape(match.link))
		outputPage.write('</tr>')
	outputPage.write('</table>')

	outputPage.write('<h2>Peer-to-peer cross-semester matches</h2>')
	outputPage.write('<table border="1">')
	for match in filter(lambda x: isinstance(x.submission1, StudentSubmission) and isinstance(x.submission2, StudentSubmission) and x.submission1.offering != x.submission2.offering, me.matches):

		# ignore if either submissino matches soln
		for match2 in me.matches:
			if (isinstance(match2.submission1, SolutionSubmission) or isinstance(match2.submission2, SolutionSubmission)) and ((match2.submission1 == match.submission1 or match2.submission1 == match.submission2) or (match2.submission1 == match.submission2 or match2.submission2 == match.submission2)):
				continue
		
		outputPage.write('<tr>')
		outputPage.write('<td>%s (%s)</td>' % (cgi.escape(repr(match.submission1)), cgi.escape(str(match.score1))))
		outputPage.write('<td>%s (%s)</td>' % (cgi.escape(repr(match.submission2)), cgi.escape(str(match.score2))))
		outputPage.write('<td><a href="%s">details</a></td>' % cgi.escape(match.link))
		outputPage.write('</tr>')
	outputPage.write('</table>')

	outputPage.write('<h2>Peer-to-peer same-semester matches</h2>')
	outputPage.write('<table border="1">')
	# should use xor
	for match in filter(lambda x: isinstance(x.submission1, StudentSubmission) and isinstance(x.submission2, StudentSubmission) and x.submission1.offering == x.submission2.offering,
			    me.matches):

		# ignore if either matches soln
		for match2 in me.matches:
			if (isinstance(match2.submission1, SolutionSubmission) or isinstance(match2.submission2, SolutionSubmission)) and ((match2.submission1 == match.submission1 or match2.submission1 == match.submission2) or (match2.submission1 == match.submission2 or match2.submission2 == match.submission2)):
				continue

		outputPage.write('<tr>')
		outputPage.write('<td>%s (%s)</td>' % (cgi.escape(repr(match.submission1)), cgi.escape(str(match.score1))))
		outputPage.write('<td>%s (%s)</td>' % (cgi.escape(repr(match.submission2)), cgi.escape(str(match.score2))))
		outputPage.write('<td><a href="%s">details</a></td>' % cgi.escape(match.link))
		outputPage.write('</tr>')
	outputPage.write('</table>')													

	outputPage.close()

	print me.workDirectory

	mr.complete = True

        # finally, copy stuff
	for sourceFile in os.listdir(me.workDirectory):
		f = open(os.path.join(me.workDirectory, sourceFile), 'r')
		content = f.read()
		f.close()
		mr.mossReportFiles.append(MossReportFile(name=sourceFile, content=content))
	
	elixir.session.commit()

	shutil.copytree(me.workDirectory, ae.webDirectory)

	print ae.webDirectory

if __name__ == '__main__':
	main()
