import { Flow } from '@vaadin/flow-frontend';
import { Route } from '@vaadin/router';
import './views/home/home-view';
import './views/main/main-view';

const { serverSideRoutes } = new Flow({
  imports: () => import('../target/frontend/generated-flow-imports'),
});

export type ViewRoute = Route & { title?: string; children?: ViewRoute[] };

export const views: ViewRoute[] = [
  // for client-side, place routes below (more info https://vaadin.com/docs/v19/flow/typescript/creating-routes.html)
  {
    path: '',
    component: 'home-view',
    title: 'Home',
  },
  {
    path: 'how-to-play',
    component: 'how-to-play-view',
    title: 'How To Play',
    action: async () => {
      await import('./views/howtoplay/how-to-play-view');
    },
  },
];
export const routes: ViewRoute[] = [
  {
    path: '',
    component: 'main-view',
    children: [
      ...views,
      // for server-side, the next magic line sends all unmatched routes:
      ...serverSideRoutes, // IMPORTANT: this must be the last entry in the array
    ],
  },
];
