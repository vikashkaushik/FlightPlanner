#!/usr/bin/env python3

"""
A simple Python script to download flight data with the world's airports and routes
and format it into a DOT file.

Requires:
  * Python 3.9+ (for some typing features)
  * graphviz (for creating a dot graph file)
  * geopy (for calculating the distance between airports)

Install all requirements with:
    sudo apt install python3-pip
    python3 -m pip install -U graphviz geopy

Run with:
python get_flight_data.py
"""

from __future__ import annotations

import argparse
import csv
import itertools
import pathlib
import sys
import urllib.request
from typing import Any, Callable, Iterable, Iterator, NamedTuple, Optional, TypeVar

import geopy.distance
import graphviz

T = TypeVar('T')

DEFAULT_OUTPUT_FILE = 'flights.gv'

AIRPORS_URL = 'https://raw.githubusercontent.com/jpatokal/openflights/master/data/airports.dat'
ROUTES_URL = 'https://raw.githubusercontent.com/jpatokal/openflights/master/data/routes.dat'


class Airport(NamedTuple):
    code: str
    latitude: float
    longitude: float

    @property
    def location(self) -> tuple[float, float]:
        return self.latitude, self.longitude


class Route(NamedTuple):
    source: str
    dest: str
    airline: str
    distance: float


def find(predicate: Callable[[T], Any], iterable: Iterable[T], /) -> Optional[T]:
    """A helper to return the first element found in the sequence
    that meets the predicate.
    """

    return next((element for element in iterable if predicate(element)), None)


def progress(iterable: Iterable[T], *, max_width: int =40) -> Iterator[T]:
    """A helper to display a progress bar over iterations through an iterable."""

    # calculate width and update interval
    iterable = list(iterable)
    length = len(iterable)
    width = min(length, max_width)
    update_interval = length // max_width

    # setup bar
    sys.stdout.write('|{}|'.format(' ' * width))
    sys.stdout.flush()
    sys.stdout.write('\b' * (width + 1))  # return to start of line, after '['

    for i, e in enumerate(iterable):
        yield e

        if i % update_interval == 0:
            # update the bar
            sys.stdout.write('\u2588')
            sys.stdout.flush()

    # end the bar
    sys.stdout.write('|\n')


def main(args: argparse.Namespace):
    pathlib.Path('data').mkdir(exist_ok=True)

    if args.skip_download:
        print('Skipping database download.')
    else:
        print('Downloading airports...')
        urllib.request.urlretrieve(AIRPORS_URL, 'data/airports.csv')
        print('Downloading routes...')
        urllib.request.urlretrieve(ROUTES_URL, 'data/routes.csv')

    airports: list[Airport] = []
    routes: list[Route] = []
    dot = graphviz.Digraph()

    # load airports into graph
    print('Loading airports...')
    with open('data/airports.csv', 'r', newline='') as f:
        for row in progress(csv.reader(f)):
            code = row[4]  # IATA code

            # only include a few airports for the partial dot file
            if args.partial and code not in ('MSN', 'MSP', 'ORD'):
                continue

            if not code or code == r'\N':  # idk what this character is
                continue

            airport = Airport(
                code=code,
                latitude=float(row[6]),
                longitude=float(row[7]),
            )
            airports.append(airport)
            dot.node(
                airport.code,
                label=row[1],  # airport name
                city=row[2],
                country=row[3],
            )

    # load routes into graph
    print('Loading routes...')
    airport_codes = [a.code for a in airports]
    with open('data/routes.csv', 'r', newline='') as f:
        for row in progress(csv.reader(f)):
            source = find(lambda a: a.code == row[2], airports)
            dest = find(lambda a: a.code == row[4], airports)

            # for some reason some of these routes have invalid airport IDs
            if source is None or dest is None:
                continue

            # calculate distance between airports
            # I could probably cache this... but I don't care enough
            distance = geopy.distance.distance(source.location, dest.location).miles
            route = Route(source=source.code, dest=dest.code,  airline=row[0], distance=distance)

            routes.append(route)

    print('Generating graph...')

    def get_key(r: Route) -> tuple[str, str]:
        return r.source, r.dest

    # group routes by their source and destination attributes
    # in order to properly format the airlines into the edge labels
    route_groups = itertools.groupby(sorted(routes, key=get_key), key=get_key)
    for (source, dest), group in route_groups:
        group = list(group)  # capture the iterator

        # format airlines into a comma-delimited list
        airlines = ','.join([r.airline for r in group])
        distance = str(group[0].distance)
        dot.edge(source, dest, weight=distance, airlines=airlines)

    print('Saving DOT file...')
    dot.save(args.output)

    print('Done!')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Downloads and formats flight data from OpenFlights.org into a DOT graph.')

    parser.add_argument(
        '-o', '--output',
        nargs='?',
        help='Output DOT file. Defaults to flights.gv',
        default=DEFAULT_OUTPUT_FILE,
        type=pathlib.Path,
    )

    parser.add_argument(
        '-s', '--skip-download',
        action='store_true',
        default=False,
    )

    parser.add_argument(
        '--partial',
        action='store_true',
        default=False,
    )

    args = parser.parse_args()
    main(args)
