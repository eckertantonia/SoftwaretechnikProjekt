import { createRouter, createWebHistory } from "vue-router";
import TitleView from "@/views/TitleView.vue";
import EditorView from "@/views/EditorView.vue";
import GameView from "@/views/GameView.vue";
import RoomSelectView from "@/views/RoomSelectView.vue";
import Error500View from "@/views/errors/Error500View.vue";
import ResetInfoView from "@/views/ResetInfoView.vue";
import Error404View from "@/views/errors/Error404View.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: "/", name: "Start", component: TitleView },
    {
      path: "/rooms",
      name: "Raumauswahl",
      component: RoomSelectView,
    },
    {
      path: "/1",
      name: "Raum1",
      component: EditorView,
    },
    {
      path: "/2",
      name: "Raum2",
      component: EditorView,
    },
    {
      path: "/3",
      name: "Raum3",
      component: EditorView,
    },
    {
      path: "/4",
      name: "Raum4",
      component: EditorView,
    },
    {
      path: "/5",
      name: "Raum5",
      component: EditorView,
    },
    {
      path: "/1/3D",
      name: "Raum1-3D",
      component: GameView,
    },
    {
      path: "/2/3D",
      name: "Raum2-3D",
      component: GameView,
    },
    {
      path: "/3/3D",
      name: "Raum3-3D",
      component: GameView,
    },
    {
      path: "/4/3D",
      name: "Raum4-3D",
      component: GameView,
    },
    {
      path: "/5/3D",
      name: "Raum5-3D",
      component: GameView,
    },
    {
      path: "/500",
      name: "genericError",
      component: Error500View,
    },
    {
      path: "/reset",
      name: "ResetInfoView",
      component: ResetInfoView,
    },
    {
      path: "/:pathMatch(.*)*",
      name: "notFound",
      component: Error404View,
    },
  ],
});

export default router;
