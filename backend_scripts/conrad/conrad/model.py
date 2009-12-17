#!/usr/bin/env python

import sqlalchemy, elixir
from sqlalchemy.types import CLOB

# http://www.sqlalchemy.org/trac/wiki/UsageRecipes/Enum
class Enum(sqlalchemy.types.TypeDecorator):
    impl = sqlalchemy.types.Unicode

    def __init__(self, values, empty_to_none=False, strict=False):
        """Emulate an Enum type.

        values:
            A list of valid values for this column
        empty_to_none:
            Optional, treat the empty string '' as None
        strict:
            Also insist that columns read from the database are in the
            list of valid values.  Note that, with strict=True, you won't
            be able to clean out bad data from the database through your
            code.
        """

        if values is None or len(values) is 0:
            raise AssertionError('Enum requires a list of values')
        self.empty_to_none = empty_to_none
        self.strict = strict
        self.values = values[:]

        # The length of the string/unicode column should be the longest string
        # in values
        size = max([len(v) for v in values if v is not None])
        super(Enum, self).__init__(size)        

    def process_bind_param(self, value, dialect):
        if self.empty_to_none and value is '':
            value = None
        if value not in self.values:
            raise AssertionError('"%s" not in Enum.values' % value)
        return value


    def process_result_value(self, value, dialect):
        if self.strict and value not in self.values:
            raise AssertionError('"%s" not in Enum.values' % value)
        return value

class User(elixir.Entity):
    name = elixir.Field(elixir.String(10), primary_key=True)
    password = elixir.Field(elixir.String(16))
    superuser = elixir.Field(elixir.Boolean())
    courses = elixir.ManyToMany('Course')

    def __repr__(self):
        return "<user %s>" % self.name

class Semester(elixir.Entity):
    season = elixir.Field(Enum([u'Spring', u'Summer', u'Fall'], strict=True),
                          primary_key=True)
    year = elixir.Field(elixir.Integer(), primary_key=True)

    def __repr__(self):
        return "<semester %s %d>" % (self.season, self.year)

    def __cmp__(self, other):
        if isinstance(other, Semester):
            if self.year == other.year:
                seasons = [u'Spring', u'Summer', u'Fall']
                return cmp(seasons.index(self.season),
                           seasons.index(other.season))
            else:
                return cmp(self.year, other.year)
        else:
            return super(self).__cmp__(other)

class Course(elixir.Entity):
    name = elixir.Field(elixir.String(10), primary_key=True)
    offerings = elixir.OneToMany('Offering', cascade='all')
    assignments = elixir.OneToMany('Assignment', cascade='all')
    users = elixir.ManyToMany('User')

    def __repr__(self):
        return "<course %s>" % self.name

class Offering(elixir.Entity):
    semester = elixir.ManyToOne('Semester', primary_key=True)
    course = elixir.ManyToOne('Course', primary_key=True)

    def __repr__(self):
        return "<offering: %s in %s>" % (self.course.name, self.semester)

class Assignment(elixir.Entity):
    course = elixir.ManyToOne('Course', primary_key=True)
    name = elixir.Field(elixir.String(30), primary_key=True)
    submissions = elixir.OneToMany('Submission', cascade='all')
    language = elixir.Field(Enum([u'cpp'], strict=True))
    analysis = elixir.OneToOne('Analysis', cascade='all')
    report = elixir.OneToOne('Report', cascade='all')

    def __repr__(self):
        return "<assignment: %s in %s>" % (self.name, self.course.name)

class Submission(elixir.Entity):
    submissionFiles = elixir.OneToMany('SubmissionFile', cascade='all')
    assignment = elixir.ManyToOne('Assignment', colname=["c","n"])
    analysisPseudonyms = elixir.ManyToOne('AnalysisPseudonym', colname=["ana","pseu"])

class StudentSubmission(Submission):
    elixir.using_options(inheritance='multi')

    # each student can have multiple submissions per (offering, assignment) --
    # e.g. early, regular, late handin
    offering = elixir.ManyToOne('Offering', colname=["seas","yr","name"])
    student = elixir.ManyToOne('Student')
    partners = elixir.ManyToMany('Student')

    def __repr__(self):
        return "<student submission: %s in %s %s %d by %s>" % (self.assignment.name,
                                                               self.offering.course.name,
                                                               self.offering.semester.season,
                                                               self.offering.semester.year,
                                                               self.student.netid)

class SolutionSubmission(Submission):
    elixir.using_options(inheritance='multi')

    def __repr__(self):
        return "<solution submission: %s>" % self.assignment.name

class SubmissionFile(elixir.Entity):
    submission = elixir.ManyToOne('Submission', primary_key=True)
    name = elixir.Field(elixir.String(40), primary_key=True)
    content = elixir.Field(CLOB)
    meta = elixir.Field(elixir.Boolean())

    def __repr__(self):
        return "<file: %s for %s>" % (self.name, self.submission)

class Student(elixir.Entity):
    # technically, NetIDs can be changed or recycled,
    # so we really should be using the student UIN
    netid = elixir.Field(elixir.String(10), primary_key=True)
    submissions = elixir.OneToMany('StudentSubmission', cascade='all')

    def __repr__(self):
        return "<student: %s>" % self.netid

class Analysis(elixir.Entity):
    assignment = elixir.ManyToOne('Assignment')
    mossAnalysis = elixir.OneToOne('MossAnalysis', inverse='analysis', cascade='all')
    jPlagAnalysis = elixir.OneToOne('JPlagAnalysis', inverse='analysis', cascade='all')
    analysisPseudonyms = elixir.OneToMany('AnalysisPseudonym', cascade='all')
    workDirectory = elixir.Field(elixir.String(255))
    complete = elixir.Field(elixir.Boolean(), default=False)
    webDirectory = elixir.Field(elixir.String(255))

    def __repr__(self):
        return "<analysis: %s>" % self.assignment

class Report(elixir.Entity):
    assignment = elixir.ManyToOne('Assignment')
    mossReport = elixir.OneToOne('MossReport', inverse='report', cascade='all')
    jPlagReport = elixir.OneToOne('JPlagReport', inverse='report', cascade='all')
    complete = elixir.Field(elixir.Boolean(), default=False)

    def __repr__(self):
        return "<report: %s>" % self.assignment

class EngineReport(elixir.Entity):
    complete = elixir.Field(elixir.Boolean(), default=False)

class MossReport(EngineReport):
    elixir.using_options(inheritance='multi')
    report = elixir.ManyToOne('Report')
    mossReportFiles = elixir.OneToMany('MossReportFile', cascade='all')

    def __repr__(self):
        return "<moss report: %s>" % self.report

class MossReportFile(elixir.Entity):
    mossReport = elixir.ManyToOne('MossReport', primary_key=True)
    name = elixir.Field(elixir.String(40), primary_key=True)
    content = elixir.Field(elixir.Binary())

    def __repr__(self):
        return "<moss report file: %s for %s>" % (self.name, self.mossReport)                

class JPlagReport(EngineReport):
    elixir.using_options(inheritance='multi')
    report = elixir.ManyToOne('Report')

    def __repr__(self):
        return "<jplag report: %s>" % self.report

class AnalysisPseudonym(elixir.Entity):
    analysis = elixir.ManyToOne('Analysis', primary_key=True)
    submission = elixir.OneToOne('Submission')
    pseudonym = elixir.Field(elixir.String(16), primary_key=True)
    #XXX elixir.using_table_options(sqlalchemy.UniqueConstraint('analysisExecution', 'submission'))

    def __repr__(self):
        return "<analysis pseudonym: %s to %s in %s>" % (self.submission, self.pseudonym, self.analysis)

class EngineAnalysis(elixir.Entity):
    workDirectory = elixir.Field(elixir.String(255))
    complete = elixir.Field(elixir.Boolean(), default=False)

class MossAnalysis(EngineAnalysis):
    elixir.using_options(inheritance='multi')
    analysis = elixir.ManyToOne('Analysis')
    matches = elixir.OneToMany('MossMatch', cascade='all')

    def __repr__(self):
        return "<moss analysis: %s>" % self.analysis

class MossMatch(elixir.Entity):
    mossAnalysis = elixir.ManyToOne('MossAnalysis')
    submission1 = elixir.ManyToOne('Submission')
    submission2 = elixir.ManyToOne('Submission')
    score1 = elixir.Field(elixir.Integer())
    score2 = elixir.Field(elixir.Integer())
    link = elixir.Field(elixir.String(255))

    def __repr__(self):
        return "<moss match: %s with %d to %s with %d>" % (self.submission1, self.score1,
                                                           self.submission2, self.score2)

class JPlagAnalysis(EngineAnalysis):
    elixir.using_options(inheritance='multi')
    analysis = elixir.ManyToOne('Analysis')

    def __repr__(self):
        return "<jplag execution: %s>" % self.analysis
