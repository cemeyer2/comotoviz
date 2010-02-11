#!/usr/bin/env python

from pymoss.model.model import *
from sqlalchemy.orm.exc import NoResultFound
import os
import tempfile
import elixir
import random
import shutil
import subprocess
#elixir.metadata.bind = "sqlite:///db"
elixir.metadata.bind = "mysql://conrad:conrad@localhost/conrad"
#elixir.metadata.bind.echo = True

def main():
	elixir.setup_all(True)

	cs225 = Course.get('cs225')
	#quadtree = Assignment.get(('cs225', 'linkedlists'))
	imgmanip = Assignment.get(('cs225', 'imagemanipulation'))
	randDir = random.randint(0,999999999)
	ae = Analysis(assignment=imgmanip,
		      workDirectory=tempfile.mkdtemp(), webDirectory='/usr/dcs/www/moss/'+str(randDir))
	nums = range(0, len(imgmanip.submissions))
	random.shuffle(nums)
	elixir.session.commit()
	allfiles = []
	for sub in imgmanip.submissions:
		am = AnalysisPseudonym(analysis=ae,
				       submission=sub,
				       pseudonym=str(nums.pop()))
		subdir = os.path.join(ae.workDirectory, am.pseudonym)
		os.mkdir(subdir)
		for subfile in filter(lambda x: not x.meta, sub.submissionFiles):
			filename = os.path.join(subdir, subfile.name)
			f = open(filename, 'w')
			f.write(subfile.content)
			f.close()
			allfiles.append(os.path.join(am.pseudonym, subfile.name))

	elixir.session.commit()
	me = MossAnalysis(analysis=ae,
			  workDirectory=tempfile.mkdtemp())
	# need to adjust current dir, -m param, language...
	mossArgs = ['/home/csgrad/cemeyer2/risc/conrad/moss', '-m', '50', '-n', '1000', '-l', 'cc', '-d']
	mossArgs.extend(allfiles)
        mossProcess = subprocess.Popen(mossArgs,
				       stdout=subprocess.PIPE,
				       stderr=subprocess.STDOUT,
				       cwd=ae.workDirectory)
	elixir.session.commit()

	mossURL = None
	while True:
		output = mossProcess.stdout.readline().strip()
		print output
		if output.startswith('http://'):
			mossURL = output
		elif output == '' and mossProcess.poll() != None:
			break

	if not mossURL:
		print 'could not locate moss results'
		return

	print mossURL

	wgetArgs = ['wget',
		    '-nd', '-p', '-k', '-r',
		    '-l', 'inf', '-m', '-np',
		    '--random-wait',
		    '--wait', '0',
		    '-D', 'moss.stanford.edu',
		    mossURL]
	wgetProcess = subprocess.Popen(wgetArgs, cwd=me.workDirectory)
	if wgetProcess.wait() != 0:
		print 'could not run wget'
		return

	ae.complete = True
	me.complete = True
	elixir.session.commit()
	print ae.complete, me.complete
	# shutil.rmdir(workdir)

if __name__ == '__main__':
	main()
