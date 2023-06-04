<template>
  <tr>
    <td class="w-28 truncate block">
      {{ userName }}
    </td>
    <td class="w-16 text-right">{{ playedHoursString }}</td>
    <td class="w-14 text-right">{{ playedMinutesString }}</td>
    <td class="w-8 text-right">{{ playedSecondsString }}</td>
  </tr>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";

let playedHoursString = ref("");
let playedMinutesString = ref("");
let playedSecondsString = ref("");

const prop = defineProps<{
  userName: string;
  userLoginTime: number;
}>();

/**
 * Calculate the played time
 *
 * @param {number} loginTime time when player logged in
 * @returns {string} played time
 */
onMounted(() => {
  setInterval(() => {
    const playedTime = new Date(
      Math.abs(new Date().getTime() - prop.userLoginTime)
    );
    // Hours -1 because timezone of central europe is +1
    playedHoursString.value = `${playedTime.getHours() - 1}h`;
    playedMinutesString.value = addLeadingZeros({
      value: playedTime.getMinutes(),
      label: "min",
    });
    playedSecondsString.value = addLeadingZeros({
      value: playedTime.getSeconds(),
      label: "s",
    });
  }, 1000);
});

/**
 * add leading zero
 *
 * @param {number} value value of time
 * @param {string} label kind of time
 * @returns {string} played time string
 */
function addLeadingZeros({ value, label }: { value: number; label: string }) {
  return value < 10 ? `0${value}${label}` : `${value}${label}`;
}
</script>
