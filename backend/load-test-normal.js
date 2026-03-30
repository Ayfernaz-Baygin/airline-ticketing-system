import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'http://airline-api-env.eba-daigpjim.eu-north-1.elasticbeanstalk.com';

export const options = {
  vus: 20,
  duration: '30s',
  thresholds: {
    http_req_failed: ['rate<0.10'],
    http_req_duration: ['p(95)<1000'],
  },
};

export default function () {
  // 1) SEARCH FLIGHTS
  const searchUrl =
    `${BASE_URL}/api/v1/flights/search` +
    `?airportFrom=IST&airportTo=ANK&numberOfPeople=1&page=0`;

  const searchRes = http.get(searchUrl, {
    headers: {
      Accept: 'application/json',
    },
  });

  check(searchRes, {
    'search status is 200': (r) => r.status === 200,
    'search response has content': (r) => {
      try {
        const body = JSON.parse(r.body);
        return Array.isArray(body.content);
      } catch (e) {
        return false;
      }
    },
  });

  // 2) CHECK-IN
  const checkInPayload = JSON.stringify({
    flightNumber: 'TK101',
    date: '2026-04-01',
    passengerName: 'Ayfernaz',
  });

  const checkInRes = http.post(
    `${BASE_URL}/api/v1/flights/check-in`,
    checkInPayload,
    {
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
    }
  );

  check(checkInRes, {
    'check-in returns acceptable status': (r) =>
      r.status === 200 || r.status === 400,
  });

  sleep(1);
}